package com.raynel.eldarwallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raynel.eldarwallet.model.Authentication
import com.raynel.eldarwallet.model.AutheticationRepoImpl
import com.raynel.eldarwallet.model.CardsRepo
import com.raynel.eldarwallet.model.UserRepo
import com.raynel.eldarwallet.model.db.Card
import com.raynel.eldarwallet.model.db.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class MainViewModel(
    private val cardsRepo: CardsRepo,
    private val userRepo: UserRepo,
    private val authenticationRepo: Authentication,
    private val email: String
): ViewModel() {

    private val _uiState : MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> get() = _uiState

    val cards: Flow<List<Card>> =
        cardsRepo.allCards(email).filterNotNull()

    val amount: Flow<String> =
        userRepo.saldo(email).filterNotNull()

    init {
        getUser(email)
    }

    private fun getUser(email: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = userRepo.getUser(email)
                _uiState.value = _uiState.value.copy(
                    addCardScreenOpen = false,
                    user = user
                )
                if(user == null) throw Exception()
            }catch (e: Exception){

            }
        }
    }

    fun openAddCardScreen(){
        _uiState.value = _uiState.value.copy(
            addCardScreenOpen = true
        )
    }

    fun addCard(name: String, cardNumber: String, lastThreeNumbers: String, dateExpired: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cardsRepo.saveNewCard(email, Card(null, email, name, cardNumber, lastThreeNumbers, dateExpired))
                _uiState.value = _uiState.value.copy(
                    addCardScreenOpen = false
                )
            }catch (e: Exception){

            }
        }
    }

    fun onLogOut(){
        viewModelScope.launch {
            try {
                authenticationRepo.logOut()
                _uiState.value = _uiState.value.copy(
                    onLogOut = true
                )
            }catch (e: Exception){

            }
        }
    }

    data class MainUiState(
        val addCardScreenOpen: Boolean = false,
        val onError: Boolean = false,
        val user: User? = null,
        val onLogOut: Boolean = false
    )

}

