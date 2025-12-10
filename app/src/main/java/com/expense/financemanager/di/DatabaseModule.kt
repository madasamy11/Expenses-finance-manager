package com.expense.financemanager.di

import android.content.Context
import androidx.room.Room
import com.expense.financemanager.data.local.ExpenseDatabase
import com.expense.financemanager.data.local.dao.CategoryDao
import com.expense.financemanager.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideExpenseDatabase(
        @ApplicationContext context: Context,
        callback: DatabaseCallback
    ): ExpenseDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            "expense_database"
        )
        .addCallback(callback)
        .build()
    }
    
    @Provides
    @Singleton
    fun provideCategoryDao(database: ExpenseDatabase): CategoryDao {
        return database.categoryDao()
    }
    
    @Provides
    @Singleton
    fun provideTransactionDao(database: ExpenseDatabase): TransactionDao {
        return database.transactionDao()
    }
}
