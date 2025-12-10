package com.expense.financemanager.data.local.dao

import androidx.room.*
import com.expense.financemanager.domain.model.Transaction
import com.expense.financemanager.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE type = :type AND date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getTransactionsByTypeAndDateRange(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): Transaction?
    
    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND date >= :startDate AND date <= :endDate")
    fun getTotalByTypeAndDateRange(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ): Flow<Double?>
    
    @Query("""
        SELECT categoryId, SUM(amount) as total 
        FROM transactions 
        WHERE type = :type AND date >= :startDate AND date <= :endDate 
        GROUP BY categoryId
    """)
    fun getCategoryTotalsByTypeAndDateRange(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ): Flow<List<CategoryTotal>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long
    
    @Update
    suspend fun updateTransaction(transaction: Transaction)
    
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
    
    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)
}

data class CategoryTotal(
    val categoryId: Long,
    val total: Double
)
