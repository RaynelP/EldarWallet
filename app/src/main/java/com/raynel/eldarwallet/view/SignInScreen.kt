package com.raynel.eldarwallet.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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

@Composable
fun AuthScreen(
    navController: NavController
){

    // obtengo el viewmodel de la primera pantalla
    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.AuthViewModelFactory(context = navController.context)
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
            uiState = uiState,
            onRegisterClick = { email, password -> viewModel.login(email, password) },
            onNavigateToRegisterScreen = { viewModel.openRegisterScreen() },
            onVerifyFields = { email, password -> viewModel.validateFields(email = email, password = password) }
        )
    } else if(!uiState.isLoginScreenOpen && uiState.isRegisterScreenOpen) {
        RegisterScreen(
            navController = navController,
            onLoginClick = { email, userName, lastName, password ->
                viewModel.register(
                    email,
                    userName,
                    lastName,
                    password
                )
            },
            onNavigateToLoginScreen = { viewModel.openLoginScreen() },
            onVerifyFields = { email, password, name, lastName ->
                viewModel.validateFields(
                    email = email,
                    password = password,
                    userName = name,
                    lastName = lastName
                )
            }
        )
    }

}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    uiState: AuthViewModel.UiLogin,
    onRegisterClick: (email: String, password: String) -> Unit,
    onNavigateToRegisterScreen: () -> Unit,
    onVerifyFields: (email: String, password: String) -> Boolean
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var enabled by remember { mutableStateOf(false) }

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
            onValueChange = {
                email = it
                enabled = onVerifyFields(
                    it,
                    password
                )
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.emailIsNotRegistrered,
        )
        if (uiState.emailIsNotRegistrered) {
            Text(
                text = "Email no registrado",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp)
                    .align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it
                enabled = onVerifyFields(
                    email,
                    it
                )
            },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = uiState.passwordIncorrect
        )
        if (uiState.passwordIncorrect) {
            Text(
                text = "Contraseña incorrecta",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp)
                    .align(Alignment.Start)
            )
        }

        Button(
            onClick = { onRegisterClick(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = enabled
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
    onNavigateToLoginScreen: () -> Unit,
    onVerifyFields: (email: String, password: String, name: String, lastName: String) -> Boolean
) {

    var email by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var enabled by remember { mutableStateOf(false) }

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
            onValueChange = {
                email = it
                enabled = onVerifyFields(
                    it,
                    password,
                    userName,
                    lastName
                )
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it
                enabled = onVerifyFields(
                    email,
                    password,
                    it,
                    lastName
                )
            },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = lastName,
            onValueChange = {
                lastName = it
                enabled = onVerifyFields(
                    email,
                    password,
                    userName,
                    it
                )
            },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it
                enabled = onVerifyFields(
                    email,
                    it,
                    userName,
                    lastName
                )
            },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = { onLoginClick(email, userName, lastName, password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = enabled
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