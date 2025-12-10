package com.expense.financemanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String, // Material icon name
    val color: Long, // Color as Long (ARGB)
    val type: TransactionType // EXPENSE or INCOME
)

enum class TransactionType {
    EXPENSE,
    INCOME
}
