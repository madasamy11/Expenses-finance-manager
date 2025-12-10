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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.financemanager.presentation.components.CategoryTotalsChart
import com.expense.financemanager.presentation.components.PeriodSelector
import com.expense.financemanager.presentation.components.TransactionItem
import com.expense.financemanager.presentation.viewmodel.IncomeViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToAddCategory: () -> Unit,
    viewModel: IncomeViewModel = hiltViewModel()
) {
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()
    val incomes by viewModel.incomes.collectAsState()
    val incomeTotal by viewModel.incomeTotal.collectAsState()
    val categoryTotals by viewModel.incomeCategoryTotals.collectAsState()
    val categories by viewModel.incomeCategories.collectAsState()
    
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.getDefault())
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Income") },
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
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total Income",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = currencyFormatter.format(incomeTotal ?: 0.0),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
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
            
            if (incomes.isEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No income for this period",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(incomes) { transaction ->
                    val category = categories.find { it.id == transaction.categoryId }
                    TransactionItem(
                        transaction = transaction,
                        category = category,
                        onEdit = { /* Transaction editing can be added in future enhancement */ },
                        onDelete = { viewModel.deleteTransaction(transaction) }
                    )
                }
            }
        }
    }
}
