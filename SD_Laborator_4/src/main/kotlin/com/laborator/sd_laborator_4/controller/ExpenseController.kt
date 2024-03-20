package com.laborator.sd_laborator_4.controller

import com.laborator.sd_laborator_4.service.ExpenseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*


@RestController
@RequestMapping("/expenses")
class ExpenseController(@Autowired private val expenseService: ExpenseService) {

    @PostMapping("/add")
    fun addExpense(@RequestBody expenseData: Map<String, Any>): ResponseEntity<Any> {
        val bigDecimalAmount = when (val amount = expenseData["amount"]) {
            is String -> try {
                BigDecimal(amount)
            } catch (e: NumberFormatException) {
                return ResponseEntity.badRequest().body("Invalid amount format")
            }
            else -> return ResponseEntity.badRequest().body("Amount must be a string representing a number")
        }


        val dateString = expenseData["date"] as String
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date : Date
        try
        {
            date = formatter.parse(dateString)
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body("Invalid date format")
        }

        expenseService.addExpense(
            expenseData["username"] as String,
            expenseData["category"] as String,
            bigDecimalAmount,
            date
        ) ?: return ResponseEntity.badRequest().body("Error adding expense")

        return ResponseEntity.ok().body("Expense added successfully")
    }


    @GetMapping("/user/{username}")
    fun getUserExpenses(@PathVariable username: String): ResponseEntity<Any> {
        val expenses = expenseService.getUserExpenses(username) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok().body(expenses)
    }

}
