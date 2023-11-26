package com.raynel.eldarwallet.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.raynel.eldarwallet.model.db.Card
import com.raynel.eldarwallet.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers

@Composable
fun HomeScreen(
    navController: NavController,
    email: String?,
){
    // obtengo el viewmodel de la primera pantalla
    val viewModel: MainViewModel = viewModel(
        factory = MainViewModel.MainViewModelFactory(
            context = navController.context,
            email!!
        )
    )

    val amount: String by viewModel.amount.collectAsState(initial = "0.00", Dispatchers.IO)
    val cards by viewModel.cards.collectAsState(initial = listOf(), Dispatchers.IO)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState){
        if(uiState.onLogOut){
            navController.navigate(AppScreen.LoginScreen.route)
        }
    }

    HomeScreen(
        uiState = uiState,
        amount = amount,
        cards = cards,
        addCard = { name, cardNumber, lastThreeNumbers, dateExpired ->
            viewModel.addCard(
                name,
                cardNumber,
                lastThreeNumbers,
                dateExpired
            )
        },
        openAddCardScreen = { viewModel.onOpenAddCardScreen() },
        onCloseSession = { viewModel.onLogOut() },
        closeAddCardScreen = { viewModel.onCloseAddCardScreen()},
        onVerifyFields = { name, cardNumber, lastThreeNumbers, dateExpired ->
            viewModel.verifyFields(
                name,
                cardNumber,
                lastThreeNumbers,
                dateExpired
            )
        },
        onPaymentScreen = {
            navController.navigate(AppScreen.PaymentScreen.route)
        },
        onQrScreen = {
            navController.navigate(AppScreen.QrScreen.route + "/${uiState.user?.name}" + "/${uiState.user?.name}")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: MainViewModel.MainUiState,
    amount: String,
    cards: List<Card>,
    addCard: (name: String, cardNumber: String, lastThreeNumbers: String, dateExpired: String) -> Unit,
    openAddCardScreen: () -> Unit,
    closeAddCardScreen: () -> Unit,
    onCloseSession: () -> Unit,
    onVerifyFields: (name: String, cardNumber: String, lastThreeNumbers: String, dateExpired: String) -> Boolean,
    onPaymentScreen: () -> Unit,
    onQrScreen: () -> Unit
) {

    Scaffold(
        topBar = {
            TopBar(
                name = uiState.user?.name ?: "",
                onCloseSession = onCloseSession,
                onBack = closeAddCardScreen,
                uiState = uiState
            )
        },

        floatingActionButton = {
            if(!uiState.addCardScreenOpen){
                FloatingActionButton(
                    onClick = { openAddCardScreen() },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    modifier = Modifier.padding(16.dp)
                ) {
                    //Icon(Icons.Filled.Add, contentDescription = "Agregar tarjeta")
                    Text(text = "Agregar tarjeta", modifier = Modifier.padding(8.dp))
                }
            }
        },

        content = {
            if (uiState.addCardScreenOpen) {
                AddCardScreen(addCard = addCard, modifier = Modifier.padding(it), verifyFields = onVerifyFields)
            } else {
                MainInfo(
                    modifier = Modifier.padding(it),
                    amount = amount,
                    cards = cards,
                    onPaymentScreen = onPaymentScreen,
                    onQrScreen = onQrScreen
                )
            }
        }
    )
}

@Composable
fun MainInfo(
    modifier: Modifier = Modifier,
    amount: String,
    cards: List<Card>,
    onPaymentScreen: () -> Unit,
    onQrScreen: () -> Unit
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Saldo: $amount",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            fontSize = 24.sp
        )

        if(cards.isEmpty()){
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                text = "Aun no has agregado ninguna tarjeta",
                style = MaterialTheme.typography.headlineMedium,
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(cards) { card ->
                    IndividualCard(card = card, onPaymentScreen = {onPaymentScreen()})
                }
            }
        }

        Button(onClick = { onQrScreen() }) {
            Text(text = "QR")
        }

    }
}

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
        modifier = Modifier.background(MaterialTheme.colorScheme.primary,),
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
        }
    )
}

val list =  listOf(
    Card(
        email = "ray@",
        name = "Raynel Jose P",
        cardNumber = "5031 7557 3453 0604",
        lastThreeNumbers = "123",
        dateExpired = "12/26",
        company = "visa"
    ),
    Card(
        email = "ray@",
        name = "Raynel Jose P",
        cardNumber = "5031 7557 3453 0604",
        lastThreeNumbers = "123",
        dateExpired = "12/26",
        company = "visa"

    ),
    Card(
        email = "ray@",
        name = "Raynel Jose P",
        cardNumber = "5031 7557 3453 0604",
        lastThreeNumbers = "123",
        dateExpired = "12/26",
        company = "visa"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndividualCard(
    modifier: Modifier = Modifier,
    card: Card,
    onPaymentScreen: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        //elevation = 4.dp,
        onClick = onPaymentScreen
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                card.name,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Número: ${card.cardNumber}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Vencimiento: ${card.dateExpired}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )

                Text(
                    "${card.company}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview() {

}

@Preview
@Composable
fun PreviewMyView() {
    IndividualCard(card = Card(null, "raynel@gmail.com", "Ray", "1515 5665 6 52562 656", "233", "", "visa"), onPaymentScreen = {})
}
