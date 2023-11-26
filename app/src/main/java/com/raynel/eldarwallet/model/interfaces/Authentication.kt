package com.raynel.eldarwallet.model.interfaces

import com.raynel.eldarwallet.model.db.User

interface Authentication {
    suspend fun login(email: String, password: String): User?
    suspend fun register(email: String, userName: String, lastName: String, password: String): String?
    suspend fun logOut()
}