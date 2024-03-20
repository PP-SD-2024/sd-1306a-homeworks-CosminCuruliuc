package com.laborator.sd_laborator_4.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.Date

@Entity
@Table(name = "expenses")
class Expense(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val category: String,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Temporal(TemporalType.DATE)
    val date: Date,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User
)
