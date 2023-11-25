package com.raynel.eldarwallet.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(
    modifier: Modifier = Modifier,
    addCard: (name: String, cardNumber: String, lastThreeNumbers: String, dateExpired: String) -> Unit
) {
    var newCardNumber by remember { mutableStateOf("") }
    var newCardDateExpired by remember { mutableStateOf("") }
    var newCardThreeNumbers by remember { mutableStateOf("") }
    var newCardName by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = {
            showBottomSheet = false
        },
        sheetState = sheetState
    ){
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Agregar nueva tarjeta",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = newCardName,
                onValueChange = { newCardName = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = newCardNumber,
                onValueChange = { newCardNumber = it },
                label = { Text("Número de tarjeta") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = newCardDateExpired,
                onValueChange = { newCardDateExpired = it },
                label = { Text("Fecha de vencimiento") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = newCardThreeNumbers,
                onValueChange = { newCardThreeNumbers = it },
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
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Agregar")
            }
        }
    }

}

@Preview
@Composable
fun PreviewSheet() {
    AddCardScreen(addCard = {name, cardNumber, lastThreeNumbers, dateExpired ->  })
}
