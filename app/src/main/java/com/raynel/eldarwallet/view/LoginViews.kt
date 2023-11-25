package com.raynel.eldarwallet.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.raynel.eldarwallet.viewmodel.AuthViewModel
import com.raynel.eldarwallet.viewmodel.AuthViewModelFactory

@Composable
fun AuthScreen(
    navController: NavController
){

    // obtengo el viewmodel de la primera pantalla
    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(context = navController.context)
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState){
        if(uiState.emailLogIn != null){
            navController.navigate(AppScreen.MainScreen.route + "/${uiState.emailLogIn}")
        }
    }

    if(uiState.isLoginScreenOpen && !uiState.isRegisterScreenOpen){
        LoginScreen(
            navController = navController,
            onRegisterClick = { email, password -> viewModel.login(email, password) },
            onNavigateToRegisterScreen = { viewModel.openRegisterScreen() }
        )
    } else if(!uiState.isLoginScreenOpen && uiState.isRegisterScreenOpen) {
        RegisterScreen(
            navController = navController,
            onLoginClick = { email, userName, lastName, password -> viewModel.register(email, userName, lastName, password)},
            onNavigateToLoginScreen = { viewModel.openLoginScreen() }
        )
    }

}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onRegisterClick: (email: String, password: String) -> Unit,
    onNavigateToRegisterScreen: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Inicio de sesión",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = { onRegisterClick(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Iniciar sesión")
        }
        TextButton(
            onClick = onNavigateToRegisterScreen,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("¿No tienes una cuenta? Regístrate")
        }
    }
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onLoginClick: (email: String, userName: String, lastName: String, password: String) -> Unit,
    onNavigateToLoginScreen: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = { onLoginClick(email, userName, lastName, password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Registrarse")
        }
        TextButton(
            onClick = onNavigateToLoginScreen,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("¿Ya tienes una cuenta? Inicia sesión")
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewLogin(){
    //LoginScreen(onRegisterClick = {email, password ->  })
}

@Composable
@Preview(showBackground = true)
fun PreviewRegister(){
    //RegisterScreen(onLoginClick = {email, userName, lastName, password ->  })
}