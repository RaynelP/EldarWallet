package com.raynel.eldarwallet.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.raynel.eldarwallet.model.interfaces.Authentication
import com.raynel.eldarwallet.model.implementations.AutheticationRepoImpl
import com.raynel.eldarwallet.model.implementations.CardRepoImp
import com.raynel.eldarwallet.model.interfaces.CardsRepo
import com.raynel.eldarwallet.model.interfaces.UserRepo
import com.raynel.eldarwallet.model.implementations.UserRepoImp
import com.raynel.eldarwallet.model.db.AppDataBase
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
        userRepo.amount(email).filterNotNull()

    init {
        getUser(email)
    }

    private fun getUser(email: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = userRepo.getUser(email)
                _uiState.value = _uiState.value.copy(
                    user = user
                )
                if(user == null) throw Exception()
            }catch (e: Exception){

            }
        }
    }

    fun addCard(name: String, cardNumber: String, lastThreeNumbers: String, dateExpired: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val firstChar = cardNumber.first()
                val firstNumber = firstChar.toString().toInt()

                val type = when(firstNumber){
                    3 -> {"American Express"}
                    4 -> {"Visa"}
                    5 -> {"Mastercard"}
                    else -> {"Desconocida"}
                }

                cardsRepo.saveNewCard(
                    email,
                    Card(null, email, name, cardNumber, lastThreeNumbers, dateExpired, type)
                )

                onCloseAddCardScreen()
            }catch (e: Exception){

            }
        }
    }

    fun verifyFields(name: String, cardNumber: String, lastThreeNumbers: String, dateExpired: String): Boolean{

        val fieldsNotEmpty = name.isNotEmpty() && cardNumber.isNotEmpty() && lastThreeNumbers.isNotEmpty() && dateExpired.isNotEmpty()
        val equalName = _uiState.value.user?.name == name

        return fieldsNotEmpty && equalName
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

    fun onOpenAddCardScreen(){
        _uiState.value = _uiState.value.copy(
            addCardScreenOpen = true
        )
    }
    fun onCloseAddCardScreen() {
        _uiState.value = _uiState.value.copy(
            addCardScreenOpen = false
        )
    }

    data class MainUiState(
        var addCardScreenOpen: Boolean = false,
        var onError: Boolean = false,
        var user: User? = null,
        var onLogOut: Boolean = false
    )

    class MainViewModelFactory(
        private val context: Context,
        private val email: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            val db = AppDataBase.getInstance(context = context)

            val cardRepo = CardRepoImp(db.cardDao())
            val userRepo = UserRepoImp(db.userDao())
            val authenticationRepo = AutheticationRepoImpl(db.userDao(), context)

            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(cardRepo, userRepo, authenticationRepo, email) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}

