package com.raynel.eldarwallet.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raynel.eldarwallet.model.AutheticationRepoImpl
import com.raynel.eldarwallet.model.CardRepoImp
import com.raynel.eldarwallet.model.CardsRepo
import com.raynel.eldarwallet.model.UserRepo
import com.raynel.eldarwallet.model.UserRepoImp
import com.raynel.eldarwallet.model.db.AppDataBase
import com.raynel.eldarwallet.model.db.User

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