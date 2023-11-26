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

        // oantalla de redireccion
        composable(AppScreen.RedirectScreen.route) {
            LaunchedEffect(Unit){
                if(email == null){
                    navController.navigate(AppScreen.LoginScreen.route)
                } else {
                    navController.navigate(AppScreen.MainScreen.route + "/$email")
                }
            }
        }

        // pantalla de registro/login
        composable(AppScreen.LoginScreen.route) {
            AuthScreen(navController = navController)
        }

        // pantalla principal
        // la pantalla principal tiene dentro la misma pantalla de agragar tarjeta usando el mismo viewmodel
        composable(
            route = AppScreen.MainScreen.route + "/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                }
            )
        ){
            val email = it.arguments?.getString("email")
            if(email != null){
                HomeScreen(
                    navController = navController,
                    email
                )
            }else{
                //error
            }
        }

        // pantalla de pago
        composable(AppScreen.PaymentScreen.route) {
            PaymentScreen(
                navController = navController,
                onBack = { navController.navigateUp() }
            )
        }

        // pantalla de qr
        composable(
            route = AppScreen.QrScreen.route + "/{name}" + "/{lastName}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                },
                navArgument("lastName") {
                    type = NavType.StringType
                }
            )) {

            val name = it.arguments?.getString("name")
            val lastName = it.arguments?.getString("lastName")
            if(name != null && lastName != null){
                QRScreen(
                    navController = navController,
                    onBackPressed = { navController.navigateUp() },
                    name = name,
                    lastName = lastName
                )
            }else{
                // error
            }

        }

    }
}

sealed class AppScreen(val route: String) {
    object LoginScreen: AppScreen("login")
    object MainScreen: AppScreen("main")
    object PaymentScreen: AppScreen("main")
    object QrScreen: AppScreen("qr")
    object RedirectScreen: AppScreen("redirect")
}