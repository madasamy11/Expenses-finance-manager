package com.expense.financemanager.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.expense.financemanager.domain.model.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class DatabaseCallback @Inject constructor(
    private val database: Provider<ExpenseDatabase>
) : RoomDatabase.Callback() {
    
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Optionally populate database with initial categories
        CoroutineScope(Dispatchers.IO).launch {
            populateInitialData()
        }
    }
    
    private suspend fun populateInitialData() {
        val db = database.get()
        
        // Add default expense categories
        val expenseCategories = listOf(
            com.expense.financemanager.domain.model.Category(
                name = "Food & Dining",
                icon = "Food",
                color = 0xFFEF5350L,
                type = TransactionType.EXPENSE
            ),
            com.expense.financemanager.domain.model.Category(
                name = "Shopping",
                icon = "Shopping",
                color = 0xFFEC407AL,
                type = TransactionType.EXPENSE
            ),
            com.expense.financemanager.domain.model.Category(
                name = "Transportation",
                icon = "Transport",
                color = 0xFF42A5F5L,
                type = TransactionType.EXPENSE
            ),
            com.expense.financemanager.domain.model.Category(
                name = "Entertainment",
                icon = "Entertainment",
                color = 0xFFAB47BCL,
                type = TransactionType.EXPENSE
            ),
            com.expense.financemanager.domain.model.Category(
                name = "Bills & Utilities",
                icon = "Bills",
                color = 0xFFFF7043L,
                type = TransactionType.EXPENSE
            )
        )
        
        // Add default income categories
        val incomeCategories = listOf(
            com.expense.financemanager.domain.model.Category(
                name = "Salary",
                icon = "Salary",
                color = 0xFF66BB6AL,
                type = TransactionType.INCOME
            ),
            com.expense.financemanager.domain.model.Category(
                name = "Investment",
                icon = "Investment",
                color = 0xFF26C6DAL,
                type = TransactionType.INCOME
            ),
            com.expense.financemanager.domain.model.Category(
                name = "Freelance",
                icon = "Other",
                color = 0xFF9CCC65L,
                type = TransactionType.INCOME
            )
        )
        
        expenseCategories.forEach { db.categoryDao().insertCategory(it) }
        incomeCategories.forEach { db.categoryDao().insertCategory(it) }
    }
}
