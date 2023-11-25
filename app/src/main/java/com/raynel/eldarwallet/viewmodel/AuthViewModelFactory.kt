package com.raynel.eldarwallet.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raynel.eldarwallet.model.Authentication
import com.raynel.eldarwallet.model.AutheticationRepoImpl
import com.raynel.eldarwallet.model.db.AppDataBase

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