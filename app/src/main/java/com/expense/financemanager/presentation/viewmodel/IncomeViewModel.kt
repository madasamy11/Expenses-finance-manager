package com.expense.financemanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expense.financemanager.data.repository.ExpenseRepository
import com.expense.financemanager.domain.model.Category
import com.expense.financemanager.domain.model.Period
import com.expense.financemanager.domain.model.Transaction
import com.expense.financemanager.domain.model.TransactionType
import com.expense.financemanager.presentation.util.DateRangeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {
    
    private val _selectedPeriod = MutableStateFlow(Period.DAY)
    val selectedPeriod: StateFlow<Period> = _selectedPeriod.asStateFlow()
    
    private val _customDateRange = MutableStateFlow<Pair<Long, Long>?>(null)
    val customDateRange: StateFlow<Pair<Long, Long>?> = _customDateRange.asStateFlow()
    
    val incomeCategories = repository.getCategoriesByType(TransactionType.INCOME)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val allCategories = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val incomes = combine(
        _selectedPeriod,
        _customDateRange
    ) { period, customRange ->
        val (startDate, endDate) = if (period == Period.CUSTOM && customRange != null) {
            customRange
        } else {
            DateRangeUtil.getDateRange(period)
        }
        repository.getTransactionsByTypeAndDateRange(TransactionType.INCOME, startDate, endDate)
    }.flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val incomeTotal = combine(
        _selectedPeriod,
        _customDateRange
    ) { period, customRange ->
        val (startDate, endDate) = if (period == Period.CUSTOM && customRange != null) {
            customRange
        } else {
            DateRangeUtil.getDateRange(period)
        }
        repository.getTotalByTypeAndDateRange(TransactionType.INCOME, startDate, endDate)
    }.flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    val incomeCategoryTotals = combine(
        _selectedPeriod,
        _customDateRange
    ) { period, customRange ->
        val (startDate, endDate) = if (period == Period.CUSTOM && customRange != null) {
            customRange
        } else {
            DateRangeUtil.getDateRange(period)
        }
        repository.getCategoryTotalsByTypeAndDateRange(TransactionType.INCOME, startDate, endDate)
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
}
