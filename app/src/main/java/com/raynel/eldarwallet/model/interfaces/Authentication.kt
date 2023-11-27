package com.raynel.eldarwallet.model.interfaces

import com.raynel.eldarwallet.model.implementations.AutheticationRepoImpl

interface Authentication {
    suspend fun login(email: String, password: String): AutheticationRepoImpl.LoginResult
    suspend fun register(email: String, userName: String, lastName: String, password: String): AutheticationRepoImpl.LoginResult
    suspend fun logOut()
}