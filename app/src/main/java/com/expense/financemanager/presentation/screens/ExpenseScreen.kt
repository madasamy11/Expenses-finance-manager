package com.expense.financemanager.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.financemanager.domain.model.Category
import com.expense.financemanager.domain.model.Transaction
import com.expense.financemanager.presentation.components.CategoryTotalsChart
import com.expense.financemanager.presentation.components.PeriodSelector
import com.expense.financemanager.presentation.components.TransactionItem
import com.expense.financemanager.presentation.viewmodel.ExpenseViewModel
import com.expense.financemanager.presentation.viewmodel.Period
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToAddCategory: () -> Unit,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    val expenseTotal by viewModel.expenseTotal.collectAsState()
    val categoryTotals by viewModel.expenseCategoryTotals.collectAsState()
    val categories by viewModel.expenseCategories.collectAsState()
    
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.getDefault())
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expenses") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SmallFloatingActionButton(
                    onClick = onNavigateToAddCategory,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(Icons.Default.Add, "Add Category")
                }
                FloatingActionButton(
                    onClick = onNavigateToAddTransaction
                ) {
                    Icon(Icons.Default.Add, "Add Transaction")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PeriodSelector(
                    selectedPeriod = selectedPeriod,
                    onPeriodSelected = { viewModel.setPeriod(it) },
                    onCustomDateRangeSelected = { start, end ->
                        viewModel.setCustomDateRange(start, end)
                    }
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total Expenses",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = currencyFormatter.format(expenseTotal ?: 0.0),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
            
            if (categoryTotals.isNotEmpty() && categories.isNotEmpty()) {
                item {
                    CategoryTotalsChart(
                        categoryTotals = categoryTotals,
                        categories = categories,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )
                }
            }
            
            item {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            if (expenses.isEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No expenses for this period",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(expenses) { transaction ->
                    val category = categories.find { it.id == transaction.categoryId }
                    TransactionItem(
                        transaction = transaction,
                        category = category,
                        onEdit = { /* TODO */ },
                        onDelete = { viewModel.deleteTransaction(transaction) }
                    )
                }
            }
        }
    }
}
