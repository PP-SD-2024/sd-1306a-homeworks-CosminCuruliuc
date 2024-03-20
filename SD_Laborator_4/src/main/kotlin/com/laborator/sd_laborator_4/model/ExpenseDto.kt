package com.laborator.sd_laborator_4.model

import java.math.BigDecimal
import java.util.Date

data class ExpenseDto(
    val date: Date,
    val amount: BigDecimal,
    val category: String
)
