package com.laborator.sd_laborator_4.model

import jakarta.persistence.*

@Entity
@Table(name = "expense_categories")
data class ExpenseCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    var name: String,

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var expenses: List<Expense> = emptyList()
)
