package com.expense.financemanager.data.repository

import com.expense.financemanager.data.local.dao.CategoryDao
import com.expense.financemanager.data.local.dao.TransactionDao
import com.expense.financemanager.domain.model.Category
import com.expense.financemanager.domain.model.Transaction
import com.expense.financemanager.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao
) {
    // Category operations
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>> =
        categoryDao.getCategoriesByType(type)
    
    fun getAllCategories(): Flow<List<Category>> =
        categoryDao.getAllCategories()
    
    suspend fun getCategoryById(id: Long): Category? =
        categoryDao.getCategoryById(id)
    
    suspend fun insertCategory(category: Category): Long =
        categoryDao.insertCategory(category)
    
    suspend fun updateCategory(category: Category) =
        categoryDao.updateCategory(category)
    
    suspend fun deleteCategory(category: Category) =
        categoryDao.deleteCategory(category)
    
    // Transaction operations
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> =
        transactionDao.getTransactionsByType(type)
    
    fun getTransactionsByTypeAndDateRange(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>> =
        transactionDao.getTransactionsByTypeAndDateRange(type, startDate, endDate)
    
    suspend fun getTransactionById(id: Long): Transaction? =
        transactionDao.getTransactionById(id)
    
    fun getTotalByTypeAndDateRange(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ): Flow<Double?> =
        transactionDao.getTotalByTypeAndDateRange(type, startDate, endDate)
    
    fun getCategoryTotalsByTypeAndDateRange(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ) = transactionDao.getCategoryTotalsByTypeAndDateRange(type, startDate, endDate)
    
    suspend fun insertTransaction(transaction: Transaction): Long =
        transactionDao.insertTransaction(transaction)
    
    suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.updateTransaction(transaction)
    
    suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.deleteTransaction(transaction)
}
