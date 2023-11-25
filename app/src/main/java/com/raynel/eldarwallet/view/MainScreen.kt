package com.raynel.eldarwallet.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.raynel.eldarwallet.R
import com.raynel.eldarwallet.model.db.Card
import com.raynel.eldarwallet.viewmodel.AuthViewModel
import com.raynel.eldarwallet.viewmodel.AuthViewModelFactory
import com.raynel.eldarwallet.viewmodel.MainViewModel
import com.raynel.eldarwallet.viewmodel.MainViewModelFactory
import kotlinx.coroutines.Dispatchers

@Composable
fun HomeScreen(
    navController: NavController,
    email: String?
){
    // obtengo el viewmodel de la primera pantalla
    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(
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
        addCard = { name, cardNumber, lastThreeNumbers, dateExpired -> viewModel.addCard(name, cardNumber, lastThreeNumbers, dateExpired) },
        openAddCardScreen = { viewModel.openAddCardScreen() },
        onCloseSession = { viewModel.onLogOut() }
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
    onCloseSession: () -> Unit
) {

    Scaffold(
        topBar = {
            TopBar(name = uiState.user?.name?: "", onCloseSession = onCloseSession)
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { openAddCardScreen() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                //Icon(Icons.Filled.Add, contentDescription = "Agregar tarjeta")
                Text(text = "Agregar tarjeta", modifier = Modifier.padding(8.dp))
            }
        },

        content = {

            Box{
                MainInfo(
                    modifier = Modifier.padding(it),
                    amount = amount,
                    cards = cards
                )

                if (uiState.addCardScreenOpen) {
                    AddCardScreen(addCard = addCard)
                }
            }

        }
    )
}

@Composable
fun MainInfo(
    modifier: Modifier = Modifier,
    amount: String,
    cards: List<Card>
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
                    IndividualCard(card = card)
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(modifier: Modifier = Modifier, name: String, onCloseSession: () -> Unit){
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
        }
    )
}

val list =  listOf(
    Card(
        email = "ray@",
        name = "Raynel Jose P",
        cardNumber = "5031 7557 3453 0604",
        lastThreeNumbers = "123",
        dateExpired = "12/26"
    ),
    Card(
        email = "ray@",
        name = "Raynel Jose P",
        cardNumber = "5031 7557 3453 0604",
        lastThreeNumbers = "123",
        dateExpired = "12/26"
    ),
    Card(
        email = "ray@",
        name = "Raynel Jose P",
        cardNumber = "5031 7557 3453 0604",
        lastThreeNumbers = "123",
        dateExpired = "12/26"
    )
)

@Composable
fun IndividualCard(
    modifier: Modifier = Modifier,
    card: Card
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        //elevation = 4.dp
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
            Text(
                "Vencimiento: ${card.dateExpired}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun PreviewMyView() {
    HomeScreen(
        uiState = MainViewModel.MainUiState(),
        amount = "0.00",
        cards = list,
        addCard = { name, cardNumber, lastThreeNumbers, dateExpired ->  },
        openAddCardScreen = { },
        onCloseSession = { }
    )
}
