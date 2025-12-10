package com.expense.financemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.expense.financemanager.presentation.navigation.Screen
import com.expense.financemanager.presentation.screens.*
import com.expense.financemanager.ui.theme.ExpenseFinanceManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseFinanceManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExpenseFinanceApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFinanceApp() {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Expenses") },
                    label = { Text("Expenses") },
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        navController.navigate(Screen.Expense.route) {
                            popUpTo(Screen.Expense.route) { inclusive = true }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AccountBalance, contentDescription = "Income") },
                    label = { Text("Income") },
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navController.navigate(Screen.Income.route) {
                            popUpTo(Screen.Income.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Expense.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Expense.route) {
                ExpenseScreen(
                    onNavigateToAddTransaction = {
                        navController.navigate(Screen.AddEditTransaction.createRoute(type = "EXPENSE"))
                    },
                    onNavigateToAddCategory = {
                        navController.navigate(Screen.AddEditCategory.createRoute(type = "EXPENSE"))
                    }
                )
            }
            
            composable(Screen.Income.route) {
                IncomeScreen(
                    onNavigateToAddTransaction = {
                        navController.navigate(Screen.AddEditTransaction.createRoute(type = "INCOME"))
                    },
                    onNavigateToAddCategory = {
                        navController.navigate(Screen.AddEditCategory.createRoute(type = "INCOME"))
                    }
                )
            }
            
            composable(
                route = Screen.AddEditCategory.route,
                arguments = listOf(
                    navArgument("categoryId") { type = NavType.LongType },
                    navArgument("type") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: -1L
                val type = backStackEntry.arguments?.getString("type") ?: "EXPENSE"
                AddEditCategoryScreen(
                    categoryId = categoryId,
                    type = type,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable(
                route = Screen.AddEditTransaction.route,
                arguments = listOf(
                    navArgument("transactionId") { type = NavType.LongType },
                    navArgument("type") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: -1L
                val type = backStackEntry.arguments?.getString("type") ?: "EXPENSE"
                AddEditTransactionScreen(
                    transactionId = transactionId,
                    type = type,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
