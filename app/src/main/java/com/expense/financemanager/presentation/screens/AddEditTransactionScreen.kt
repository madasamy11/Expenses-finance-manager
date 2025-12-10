package com.expense.financemanager.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.financemanager.domain.model.Transaction
import com.expense.financemanager.domain.model.TransactionType
import com.expense.financemanager.presentation.viewmodel.ExpenseViewModel
import com.expense.financemanager.presentation.viewmodel.IncomeViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    transactionId: Long,
    type: String,
    onNavigateBack: () -> Unit,
    expenseViewModel: ExpenseViewModel = hiltViewModel(),
    incomeViewModel: IncomeViewModel = hiltViewModel()
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    
    val transactionType = remember { 
        if (type == "INCOME") TransactionType.INCOME else TransactionType.EXPENSE 
    }
    
    // Use the appropriate ViewModel based on transaction type
    val categories = if (transactionType == TransactionType.INCOME) {
        incomeViewModel.incomeCategories.collectAsState()
    } else {
        expenseViewModel.expenseCategories.collectAsState()
    }
    
    val filteredCategories by categories
    
    // Select the appropriate ViewModel for operations
    val viewModel = if (transactionType == TransactionType.INCOME) incomeViewModel else expenseViewModel
    
    val dateFormatter = remember {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (transactionId == -1L) "Add Transaction" else "Edit Transaction") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { 
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                        amount = it
                    }
                },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("$") }
            )
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Category Selector
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = filteredCategories.find { it.id == selectedCategoryId }?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    filteredCategories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                selectedCategoryId = category.id
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            // Date Picker (using current implementation with timestamp)
            OutlinedTextField(
                value = dateFormatter.format(Date(selectedDate)),
                onValueChange = {},
                readOnly = true,
                label = { Text("Date") },
                trailingIcon = {
                    IconButton(onClick = { /* Date picker can be added in future enhancement */ }) {
                        Icon(Icons.Default.DateRange, "Select Date")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (amountValue != null && amountValue > 0 && selectedCategoryId != null) {
                        val transaction = Transaction(
                            id = if (transactionId == -1L) 0 else transactionId,
                            amount = amountValue,
                            categoryId = selectedCategoryId!!,
                            description = description,
                            date = selectedDate,
                            type = transactionType
                        )
                        if (transactionId == -1L) {
                            viewModel.insertTransaction(transaction)
                        } else {
                            viewModel.updateTransaction(transaction)
                        }
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = amount.toDoubleOrNull()?.let { it > 0 } == true && selectedCategoryId != null
            ) {
                Text("Save Transaction")
            }
        }
    }
}
