package com.raynel.eldarwallet.view

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.raynel.eldarwallet.viewmodel.MainViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    name: String,
    onCloseSession: () -> Unit,
    uiState: MainViewModel.MainUiState,
    onBack: () -> Unit,
){
    TopAppBar(
        title = { Text("Hola, $name") },
        actions = {
            Row(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                // Otros elementos de la barra superior

                // Botón en la parte derecha
                IconButton(onClick = { onCloseSession() }) {
                    Text(text = "Salir")
                }
            }
        },
        navigationIcon = {
            if(uiState.addCardScreenOpen){
                IconButton(onClick = { onBack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Botón de retroceso")
                }
            }
        },
    )
}

@Preview
@Composable
fun sfreview(){
    TopBar(name = "hOLA", onCloseSession = { /*TODO*/ }, uiState = MainViewModel.MainUiState()) {

    }
}