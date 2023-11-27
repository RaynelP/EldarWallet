package com.raynel.eldarwallet.model.interfaces

import com.raynel.eldarwallet.model.db.entities.User
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    suspend fun getUser(email: String): User?
    fun amount(email: String): Flow<String?>
}