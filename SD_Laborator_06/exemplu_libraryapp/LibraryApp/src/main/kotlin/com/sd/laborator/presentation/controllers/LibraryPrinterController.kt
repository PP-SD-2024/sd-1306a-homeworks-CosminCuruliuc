package com.sd.laborator.presentation.controllers

import com.sd.laborator.business.interfaces.ILibraryDAOService
import com.sd.laborator.business.interfaces.ILibraryPrinterService
import com.sd.laborator.presentation.config.RabbitMqComponent
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Controller
class LibraryPrinterController {
    @Autowired
    private lateinit var _libraryDAOService: ILibraryDAOService

    @Autowired
    private lateinit var _libraryPrinterService: ILibraryPrinterService

    @Autowired
    private lateinit var rabbitMqComponent: RabbitMqComponent

    @Autowired
    private lateinit var amqpTemplate: AmqpTemplate

    @Autowired
    fun initTemplate() {
        this.amqpTemplate = rabbitMqComponent.rabbitTemplate()
        clearQueue()
    }

    @RequestMapping("/print", method = [RequestMethod.GET])
    @ResponseBody
    fun customPrint(@RequestParam(required = true, name = "format", defaultValue = "") format: String): String {

        val rabbitQuery = "/print?format=$format"
        sendMessage("__get__~~__query__==$rabbitQuery")

        val parameters = receiveMessage()
        val pairAnalyzer = isQueryFresh(parameters)
        if (pairAnalyzer.first){
            println("This operation is fresh")
            return pairAnalyzer.second
        }

        val result =  when(format) {
            "html" -> _libraryPrinterService.printHTML(_libraryDAOService.getBooks().toSet())
            "json" -> _libraryPrinterService.printJSON(_libraryDAOService.getBooks().toSet())
            "raw" -> _libraryPrinterService.printRaw(_libraryDAOService.getBooks().toSet())
            else -> "Not implemented"
        }
        sendMessage("__insert__~~__query__==$rabbitQuery;;__result__==$result")
        val receivedInsertionMessage = receiveMessage()
        println("Insertion callback: $receivedInsertionMessage")
        return result
    }

    @RequestMapping("/find", method = [RequestMethod.GET])
    @ResponseBody
    fun customFind(@RequestParam(required = false, name = "author", defaultValue = "") author: String,
                   @RequestParam(required = false, name = "title", defaultValue = "") title: String,
                   @RequestParam(required = false, name = "publisher", defaultValue = "") publisher: String): String {
        val rabbitQuery = "/find?author=$author&title=$title&publisher=$publisher"
        sendMessage("__get__~~__query__==$rabbitQuery")
        val parameters = receiveMessage()
        val pairAnalyzer = isQueryFresh(parameters)
        if (pairAnalyzer.first){
            println("This operation is fresh")
            return pairAnalyzer.second
        }
        var result: String
        if (author != "")
            result = this._libraryPrinterService.printJSON(this._libraryDAOService.findAllByAuthor(author).toSet())
        else if (title != "")
            result = this._libraryPrinterService.printJSON(this._libraryDAOService.findAllByTitle(title).toSet())
        else if (publisher != "")
            result = this._libraryPrinterService.printJSON(this._libraryDAOService.findAllByPublisher(publisher).toSet())
        else result =  "Not a valid field"

        sendMessage("__insert__~~__query__==$rabbitQuery;;__result__==$result")
        val receivedInsertionMessage = receiveMessage()
        println("Insertion callback: $receivedInsertionMessage")
        return result
    }

    @RequestMapping("/find_and_print", method = [RequestMethod.GET])
    @ResponseBody
    fun customFindAndPrint(@RequestParam(required = true, name = "format", defaultValue = "") format: String,
                           @RequestParam(required = false, name = "author", defaultValue = "") author: String,
                           @RequestParam(required = false, name = "title", defaultValue = "") title: String,
                           @RequestParam(required = false, name = "publisher", defaultValue = "") publisher: String): String {
        val rabbitQuery = "/find_and_print?format=$format&author=$author&title=$title&publisher=$publisher"
        sendMessage("__get__~~__query__==$rabbitQuery")
        val parameters = receiveMessage()
        val pairAnalyzer = isQueryFresh(parameters)
        if (pairAnalyzer.first){
            println("This operation is fresh")
            return pairAnalyzer.second
        }
        var result: String
        val lambdaFunction =  when(format) {
            "html" -> _libraryPrinterService::printHTML
            "json" -> _libraryPrinterService::printJSON
            "raw" -> _libraryPrinterService::printRaw
            else -> return "Not implemented"
        }

        result = if (author != "")
            lambdaFunction(this._libraryDAOService.findAllByAuthor(author).toSet())
        else if (title != "")
            lambdaFunction(this._libraryDAOService.findAllByTitle(title).toSet())
        else if (publisher != "")
            lambdaFunction(this._libraryDAOService.findAllByPublisher(publisher).toSet())
        else "Not a valid field"

        sendMessage("__insert__~~__query__==$rabbitQuery;;__result__==$result")
        val receivedInsertionMessage = receiveMessage()
        println("Insertion callback: $receivedInsertionMessage")
        return result
    }

    fun isQueryFresh(parameters: String) : Pair<Boolean, String>{
        println(parameters)
        if (parameters == "invalid_request" || parameters == "empty_result" || parameters == "not_succesful_operation") {
            return Pair(false, "null")
        }
        var time: String? = null
        var queryResult: String? = null

        val delimiter_1 = """__time__=="""
        val delimiter_2 = """__result__=="""

        val split = parameters.split(";;")
        for (item in split) {
            if (item.contains(delimiter_1)) {
                time = item.split(delimiter_1)[1]
            } else if (item.contains(delimiter_2)) {
                queryResult = item.split(delimiter_2)[1]
            }
        }
        println("time = $time")
        println("QueryResult = $queryResult")
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val dateTime = LocalDateTime.parse(time, formatter)
            val now = LocalDateTime.now()

            val hours: Long = dateTime.until(now, ChronoUnit.HOURS)
            if (hours < 1) {
                return Pair(true, queryResult ?: "null")
            }
        } catch (e: Exception) {

        }
        return Pair(false, "null")
    }

    fun receiveMessage() : String {
        var msg = this.amqpTemplate.receive("library.queue")
        //if queue is empty receive will get null
        while (msg == null){
            msg = this.amqpTemplate.receive("library.queue")
        }
        println(msg.body)
        val processed_msg = (msg.body.map { it.toInt().toChar() }).joinToString(separator = "")
        return processed_msg
    }

    fun clearQueue() {
        while(this.amqpTemplate.receive("library.queue") != null);
        println("Cleared Queue")
    }

    fun sendMessage(msg: String) : String{
        println("message: $msg")
        return this.amqpTemplate.convertAndSend(rabbitMqComponent.getExchange(), rabbitMqComponent.getRoutingKey(), msg).toString()
    }

}