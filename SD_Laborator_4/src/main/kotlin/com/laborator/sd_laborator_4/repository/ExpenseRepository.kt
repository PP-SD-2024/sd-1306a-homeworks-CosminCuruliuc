package com.laborator.sd_laborator_4.repository

import com.laborator.sd_laborator_4.model.Expense
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ExpenseRepository : CrudRepository<Expense, Long> {
    fun findByUserId(userId: Long): List<Expense>
}
