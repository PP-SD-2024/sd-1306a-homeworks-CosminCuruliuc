package com.sd.laborator.components

import com.sd.laborator.interfaces.LibraryDAO
import com.sd.laborator.interfaces.LibraryPrinter
import com.sd.laborator.model.Book
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.Exception

@Component
class LibraryAppComponent {
    @Autowired
    private lateinit var libraryDAO: LibraryDAO

    @Autowired
    private lateinit var libraryPrinter: LibraryPrinter

    @Autowired
    private lateinit var connectionFactory: RabbitMqConnectionFactoryComponent

    private lateinit var amqpTemplate: AmqpTemplate

    @Autowired
    fun initTemplate() {
        this.amqpTemplate = connectionFactory.rabbitTemplate()
    }

    fun sendMessage(msg: String) {
        this.amqpTemplate.convertAndSend(connectionFactory.getExchange(),
                                         connectionFactory.getRoutingKey(),
                                         msg)
    }

    @RabbitListener(queues = ["\${libraryapp.rabbitmq.queue}"])
    fun recieveMessage(msg: String) {
        // the result needs processing
        val processedMsg = (msg.split(",").map { it.toInt().toChar() }).joinToString(separator = "")
        try {
            var format = ""
            var findBy = ""
            for (line in processedMsg.lines()) {
                val (function, parameter) = line.split(":")
                when (function) {
                    "print" -> format = parameter
                    "find" -> findBy = parameter
                }
            }
            val result = getBooksByFormat(format, findBy)
            sendMessage(result)
        } catch (e: Exception) {
            println(e)
        }
    }

    private fun getBooksByFormat(format: String, findBy: String): String {
        var books: Set<Book>? = null
        if(findBy.isNotEmpty()) {
            val (field, value) = findBy.split("=")
            books = when(field) {
                "author" -> libraryDAO.findAllByAuthor(value)
                "title" -> libraryDAO.findAllByTitle(value)
                "publisher" -> libraryDAO.findAllByPublisher(value)
                else -> null
            }
        }
        if(books == null) {
            books = libraryDAO.getBooks()
        }

        return when(format) {
            "html" -> libraryPrinter.printHTML(libraryDAO.getBooks())
            "json" -> libraryPrinter.printJSON(libraryDAO.getBooks())
            "raw" -> libraryPrinter.printRaw(libraryDAO.getBooks())
            else -> "Not implemented"
        }
    }

    fun addBook(book: Book): Boolean {
        return try {
            this.libraryDAO.addBook(book)
            true
        } catch (e: Exception) {
            false
        }
    }

}