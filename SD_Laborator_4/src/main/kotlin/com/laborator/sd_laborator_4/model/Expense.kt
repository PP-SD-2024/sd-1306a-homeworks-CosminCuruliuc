package com.laborator.sd_laborator_4.model

import java.util.*
import jakarta.persistence.*

@Entity
@Table(name = "expenses")
data class Expense(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @Column(nullable = false)
    var amount: Double,

    @Column(nullable = false)
    var description: String,

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    var date: Date,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: ExpenseCategory
)
