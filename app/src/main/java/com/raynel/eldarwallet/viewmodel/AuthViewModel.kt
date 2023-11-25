package com.raynel.eldarwallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raynel.eldarwallet.model.Authentication
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
}