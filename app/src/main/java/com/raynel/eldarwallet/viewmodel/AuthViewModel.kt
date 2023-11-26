package com.raynel.eldarwallet.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.raynel.eldarwallet.model.interfaces.Authentication
import com.raynel.eldarwallet.model.implementations.AutheticationRepoImpl
import com.raynel.eldarwallet.model.db.AppDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authenticationRepo: Authentication): ViewModel() {

    private val _uiState : MutableStateFlow<UiLogin> = MutableStateFlow(UiLogin())
    val uiState: StateFlow<UiLogin> get() = _uiState

    fun login(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            val user = authenticationRepo.login(email, password)
            if(user == null){
                // no esta registrado
            } else {
                _uiState.value = _uiState.value.copy(
                    emailLogIn = user.email
                )
            }
        }
    }

    fun register(email: String, userName: String, lastName: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            val emailRegistrered = authenticationRepo.register(email, userName, lastName, password)
            if(emailRegistrered != null){
                _uiState.value = _uiState.value.copy(
                    emailLogIn = emailRegistrered
                )
            }
        }
    }

    fun openLoginScreen(){
        _uiState.value = _uiState.value.copy(
            isLoginScreenOpen = true,
            isRegisterScreenOpen = false
        )
    }

    fun openRegisterScreen(){
        _uiState.value = _uiState.value.copy(
            isLoginScreenOpen = false,
            isRegisterScreenOpen = true
        )
    }

    data class UiLogin(
        val isLoginScreenOpen: Boolean = true,
        val isRegisterScreenOpen: Boolean = false,
        val emailLogIn: String? = null
    )

    class AuthViewModelFactory(
        val context: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            val db = AppDataBase.getInstance(context = context)
            val authenticationRepo = AutheticationRepoImpl(db.userDao(), context)

            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                return AuthViewModel(authenticationRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}