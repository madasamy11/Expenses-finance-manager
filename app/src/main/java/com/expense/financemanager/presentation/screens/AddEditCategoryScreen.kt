package com.expense.financemanager.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.expense.financemanager.domain.model.Category
import com.expense.financemanager.domain.model.TransactionType
import com.expense.financemanager.presentation.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCategoryScreen(
    categoryId: Long,
    type: String,
    onNavigateBack: () -> Unit,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("Shopping") }
    var selectedColor by remember { mutableStateOf(Color(0xFFEF5350)) }
    val transactionType = remember { 
        if (type == "INCOME") TransactionType.INCOME else TransactionType.EXPENSE 
    }
    
    val availableIcons = listOf(
        "Shopping", "Food", "Transport", "Entertainment", 
        "Health", "Education", "Bills", "Salary",
        "Investment", "Gift", "Other", "Home"
    )
    
    val availableColors = listOf(
        Color(0xFFEF5350), Color(0xFFEC407A), Color(0xFFAB47BC),
        Color(0xFF7E57C2), Color(0xFF5C6BC0), Color(0xFF42A5F5),
        Color(0xFF29B6F6), Color(0xFF26C6DA), Color(0xFF26A69A),
        Color(0xFF66BB6A), Color(0xFF9CCC65), Color(0xFFFFEE58),
        Color(0xFFFFCA28), Color(0xFFFF7043), Color(0xFF8D6E63)
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (categoryId == -1L) "Add Category" else "Edit Category") },
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Category Name") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Select Icon",
                    style = MaterialTheme.typography.titleMedium
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    items(availableIcons) { icon ->
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(
                                    color = if (icon == selectedIcon) 
                                        MaterialTheme.colorScheme.primaryContainer 
                                    else 
                                        MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clickable { selectedIcon = icon }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = icon.first().toString(),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Select Color",
                    style = MaterialTheme.typography.titleMedium
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(150.dp)
                ) {
                    items(availableColors) { color ->
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(color = color, shape = CircleShape)
                                .clickable { selectedColor = color }
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (color == selectedColor) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
            
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val category = Category(
                            id = if (categoryId == -1L) 0 else categoryId,
                            name = name,
                            icon = selectedIcon,
                            color = selectedColor.toArgb().toLong(),
                            type = transactionType
                        )
                        if (categoryId == -1L) {
                            viewModel.insertCategory(category)
                        } else {
                            viewModel.updateCategory(category)
                        }
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank()
            ) {
                Text("Save Category")
            }
        }
    }
}
