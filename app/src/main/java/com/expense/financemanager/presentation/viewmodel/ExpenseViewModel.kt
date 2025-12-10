package com.expense.financemanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.financemanager.data.repository.ExpenseRepository
import com.expense.financemanager.domain.model.Category
import com.expense.financemanager.domain.model.Transaction
import com.expense.financemanager.domain.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {
    
    private val _selectedPeriod = MutableStateFlow(Period.DAY)
    val selectedPeriod: StateFlow<Period> = _selectedPeriod.asStateFlow()
    
    private val _customDateRange = MutableStateFlow<Pair<Long, Long>?>(null)
    val customDateRange: StateFlow<Pair<Long, Long>?> = _customDateRange.asStateFlow()
    
    val expenseCategories = repository.getCategoriesByType(TransactionType.EXPENSE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val allCategories = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val expenses = combine(
        _selectedPeriod,
        _customDateRange
    ) { period, customRange ->
        val (startDate, endDate) = if (period == Period.CUSTOM && customRange != null) {
            customRange
        } else {
            getDateRange(period)
        }
        repository.getTransactionsByTypeAndDateRange(TransactionType.EXPENSE, startDate, endDate)
    }.flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val expenseTotal = combine(
        _selectedPeriod,
        _customDateRange
    ) { period, customRange ->
        val (startDate, endDate) = if (period == Period.CUSTOM && customRange != null) {
            customRange
        } else {
            getDateRange(period)
        }
        repository.getTotalByTypeAndDateRange(TransactionType.EXPENSE, startDate, endDate)
    }.flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    val expenseCategoryTotals = combine(
        _selectedPeriod,
        _customDateRange
    ) { period, customRange ->
        val (startDate, endDate) = if (period == Period.CUSTOM && customRange != null) {
            customRange
        } else {
            getDateRange(period)
        }
        repository.getCategoryTotalsByTypeAndDateRange(TransactionType.EXPENSE, startDate, endDate)
    }.flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun setPeriod(period: Period) {
        _selectedPeriod.value = period
    }
    
    fun setCustomDateRange(startDate: Long, endDate: Long) {
        _customDateRange.value = Pair(startDate, endDate)
        _selectedPeriod.value = Period.CUSTOM
    }
    
    fun insertCategory(category: Category) = viewModelScope.launch {
        repository.insertCategory(category)
    }
    
    fun updateCategory(category: Category) = viewModelScope.launch {
        repository.updateCategory(category)
    }
    
    fun deleteCategory(category: Category) = viewModelScope.launch {
        repository.deleteCategory(category)
    }
    
    fun insertTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.insertTransaction(transaction)
    }
    
    fun updateTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.updateTransaction(transaction)
    }
    
    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.deleteTransaction(transaction)
    }
    
    private fun getDateRange(period: Period): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.timeInMillis
        
        when (period) {
            Period.DAY -> {
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
            }
            Period.WEEK -> {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
            }
            Period.MONTH -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
            }
            Period.YEAR -> {
                calendar.set(Calendar.DAY_OF_YEAR, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
            }
            Period.CUSTOM -> {} // Handled separately
        }
        
        return Pair(calendar.timeInMillis, endDate)
    }
}

enum class Period {
    DAY, WEEK, MONTH, YEAR, CUSTOM
}
