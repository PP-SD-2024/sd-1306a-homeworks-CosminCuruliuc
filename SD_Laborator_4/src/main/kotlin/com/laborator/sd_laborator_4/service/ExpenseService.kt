package com.laborator.sd_laborator_4.service

import com.laborator.sd_laborator_4.model.Expense
import com.laborator.sd_laborator_4.model.ExpenseDto
import com.laborator.sd_laborator_4.repository.ExpenseRepository
import com.laborator.sd_laborator_4.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class ExpenseService(
    @Autowired private val expenseRepository: ExpenseRepository,
    @Autowired private val userRepository: UserRepository
) {
    fun addExpense(username: String, category: String, amount: BigDecimal, date: Date): Expense? {
        val user = userRepository.findByUsername(username) ?: return null
        val expense = Expense(category = category, amount = amount, date = date, user = user)
        return expenseRepository.save(expense)
    }

    fun getUserExpenses(username: String): List<ExpenseDto>? {
        val user = userRepository.findByUsername(username) ?: return null
        return expenseRepository.findByUserId(user.id).map { expense ->
            ExpenseDto(date = expense.date, amount = expense.amount, category = expense.category)
        }
    }

}
