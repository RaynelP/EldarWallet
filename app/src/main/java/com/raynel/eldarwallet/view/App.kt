package com.raynel.eldarwallet.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun MyApp(email: String?) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = AppScreen.RedirectScreen.route) {

        composable(AppScreen.RedirectScreen.route) {
            LaunchedEffect(Unit){
                if(email == null){
                    navController.navigate(AppScreen.LoginScreen.route)
                } else {
                    navController.navigate(AppScreen.MainScreen.route + "/$email")
                }
            }
        }

        composable(AppScreen.LoginScreen.route) {
            AuthScreen(navController = navController)
        }

        composable(
            route = AppScreen.MainScreen.route + "/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                }
            )
        ){
            HomeScreen(navController = navController, it.arguments?.getString("email")?: email)
        }

    }
}

sealed class AppScreen(val route: String) {
    object LoginScreen: AppScreen("login")
    object MainScreen: AppScreen("main")
    object RedirectScreen: AppScreen("redirect")
}