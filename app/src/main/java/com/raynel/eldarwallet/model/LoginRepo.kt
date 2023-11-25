package com.raynel.eldarwallet.model

import com.raynel.eldarwallet.model.db.User

interface LoginRepo {
    suspend fun createUser(user: User): User
    suspend fun login(user: User): User
    suspend fun deleteUser(user: User): Boolean
}