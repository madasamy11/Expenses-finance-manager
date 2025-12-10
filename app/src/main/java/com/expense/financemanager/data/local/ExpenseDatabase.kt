package com.expense.financemanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.expense.financemanager.data.local.dao.CategoryDao
import com.expense.financemanager.data.local.dao.TransactionDao
import com.expense.financemanager.domain.model.Category
import com.expense.financemanager.domain.model.Transaction

@Database(
    entities = [Category::class, Transaction::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}
