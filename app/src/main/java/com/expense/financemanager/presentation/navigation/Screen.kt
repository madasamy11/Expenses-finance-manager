package com.expense.financemanager.presentation.navigation

sealed class Screen(val route: String) {
    object Expense : Screen("expense")
    object Income : Screen("income")
    object AddEditCategory : Screen("add_edit_category/{categoryId}/{type}") {
        fun createRoute(categoryId: Long? = null, type: String) = 
            "add_edit_category/${categoryId ?: -1}/$type"
    }
    object AddEditTransaction : Screen("add_edit_transaction/{transactionId}/{type}") {
        fun createRoute(transactionId: Long? = null, type: String) = 
            "add_edit_transaction/${transactionId ?: -1}/$type"
    }
}
