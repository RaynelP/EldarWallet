package com.raynel.eldarwallet.model

import com.raynel.eldarwallet.model.db.User
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    suspend fun getUser(email: String): User?
    fun saldo(email: String): Flow<String?>
}