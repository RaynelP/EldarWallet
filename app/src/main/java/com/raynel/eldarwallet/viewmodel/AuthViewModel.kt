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

    // estado de vista del viewmodel
    private val _uiState : MutableStateFlow<UiLogin> = MutableStateFlow(UiLogin())
    val uiState: StateFlow<UiLogin> get() = _uiState

    fun login(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = authenticationRepo.login(email, password)
                if(result is AutheticationRepoImpl.LoginResult.Successful){
                    _uiState.value = _uiState.value.copy(
                        emailLogIn = result.email
                    )
                }else if(result is AutheticationRepoImpl.LoginResult.EmailIsNotRegister){
                    _uiState.value = _uiState.value.copy(
                        emailIsNotRegistrered = true
                    )
                } else if(result is AutheticationRepoImpl.LoginResult.PasswordIncorrect){
                    _uiState.value = _uiState.value.copy(
                        passwordIncorrect = true
                    )
                }
            }catch (e: Exception){
                // hubo un error
            }
        }
    }

    fun register(email: String, userName: String, lastName: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = authenticationRepo.register(email, userName, lastName, password)

                if(result is AutheticationRepoImpl.LoginResult.Successful){
                    _uiState.value = _uiState.value.copy(
                        emailLogIn = result.email
                    )
                }

                else {}// hubo un error
            }catch (e: Exception){
                // hubo un error
            }

        }
    }

    fun validateFields(email: String, password: String, userName: String = "1", lastName: String = "1"): Boolean{
        _uiState.value = _uiState.value.copy(
            emailIsNotRegistrered = false,
            passwordIncorrect = false
        )
        return email.isNotEmpty() && password.isNotEmpty() && userName.isNotEmpty() && lastName.isNotEmpty()
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
        val emailIsNotRegistrered: Boolean = false,
        val passwordIncorrect: Boolean = false,
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