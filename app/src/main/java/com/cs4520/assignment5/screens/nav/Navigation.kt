package com.cs4520.assignment5.screens.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cs4520.assignment5.screens.login.LoginScreen
import com.cs4520.assignment5.screens.productlist.ProductListScreen

@Composable
fun SetupNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.ProductList.route) {
            ProductListScreen()
        }
        // ... add other composable screens here
    }
}

enum class Screen(val route: String) {
    Login("login"),
    ProductList("productList")
    // ... define other routes here
}