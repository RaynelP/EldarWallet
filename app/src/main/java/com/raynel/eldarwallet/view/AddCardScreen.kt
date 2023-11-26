package com.raynel.eldarwallet.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AddCardScreen(
    modifier: Modifier = Modifier,
    addCard: (name: String, cardNumber: String, lastThreeNumbers: String, dateExpired: String) -> Unit,
    verifyFields: (name: String, cardNumber: String, lastThreeNumbers: String, dateExpired: String) -> Boolean
) {

    var newCardNumber by remember { mutableStateOf("") }
    var newCardDateExpired by remember { mutableStateOf("") }
    var newCardThreeNumbers by remember { mutableStateOf("") }
    var newCardName by remember { mutableStateOf("") }
    var enabled by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        Text(
            "Agregar nueva tarjeta",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )

        OutlinedTextField(
            value = newCardName,
            onValueChange = {
                newCardName = it
                enabled = verifyFields(
                    it,
                    newCardNumber,
                    newCardThreeNumbers,
                    newCardDateExpired
                )
            },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = newCardNumber,
            onValueChange = { newCardNumber = it
                enabled = verifyFields(
                    newCardName,
                    it,
                    newCardThreeNumbers,
                    newCardDateExpired
                )
            },
            label = { Text("Número de tarjeta") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = newCardDateExpired,
            onValueChange = { newCardDateExpired = it
                enabled = verifyFields(
                    newCardName,
                    newCardNumber,
                    newCardThreeNumbers,
                    it
                )
            },
            label = { Text("Fecha de vencimiento") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = newCardThreeNumbers,
            onValueChange = { newCardThreeNumbers = it
                enabled = verifyFields(
                    newCardName,
                    newCardNumber,
                    it,
                    newCardDateExpired
                )
            },
            label = { Text("CVV") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                addCard(
                    newCardName,
                    newCardNumber,
                    newCardThreeNumbers,
                    newCardDateExpired
                )
            },
            modifier = Modifier.align(Alignment.End),
            enabled = enabled
        ) {
            Text("Agregar")
        }

        Text(
            "*Todos los campos no deben estar vacios",
        )
        Text(
            "*El nombre debe coincidir",
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewSheet() {
    AddCardScreen(addCard = {name, cardNumber, lastThreeNumbers, dateExpired ->  }, verifyFields = {name, cardNumber, lastThreeNumbers, dateExpired ->  true})
}
