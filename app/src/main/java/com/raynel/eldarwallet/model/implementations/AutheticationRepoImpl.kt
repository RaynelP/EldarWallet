package com.raynel.eldarwallet.model.implementations

import android.content.Context
import android.content.SharedPreferences
import com.raynel.eldarwallet.model.db.entities.User
import com.raynel.eldarwallet.model.db.daos.UserDao
import com.raynel.eldarwallet.model.interfaces.Authentication
import java.lang.Exception

class AutheticationRepoImpl(private val userRepo: UserDao, private val context: Context) :
    Authentication {

    companion object{
        private const val AUTH_PREFERENCE = "auth"
        fun init(context: Context): SharedPreferences {
            return context.getSharedPreferences(AUTH_PREFERENCE, Context.MODE_PRIVATE)
        }
    }

    override suspend fun login(email: String, password: String): LoginResult {
        try {
            val user = userRepo.findUserByEmail(email) ?: throw Exception()
            if(user.password == password){
                init(context).edit().putString("email", email).apply()
                return LoginResult.Successful(user.email)
            } else {
                return LoginResult.PasswordIncorrect
            }
        }catch (e: Exception){
            // no esta registrado
            return LoginResult.EmailIsNotRegister
        }
    }

    override suspend fun register(email: String, userName: String, lastName: String, password: String): LoginResult {
        val user = userRepo.findUserByEmail(email)
        if(user != null){
            return LoginResult.EmailIsAlredyRegistred
        } else {
            userRepo.insert(
                User(
                    email = email,
                    name = userName,
                    lastName = lastName,
                    password = password,
                    amount = "0.00"
                )
            )
            init(context).edit().putString("email", email).apply()
            return LoginResult.Successful(email)
        }
    }

    override suspend fun logOut() {
        init(context).edit().putString("email", null).apply()
    }

    sealed class LoginResult {
        object PasswordIncorrect: LoginResult()
        object Error: LoginResult()
        object EmailIsNotRegister: LoginResult()
        object EmailIsAlredyRegistred: LoginResult()
        class Successful(val email: String) : LoginResult()
    }
}