package com.raynel.eldarwallet.model

import com.raynel.eldarwallet.model.db.User
import com.raynel.eldarwallet.model.db.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepoImp(private val userDao: UserDao): UserRepo {
    override suspend fun getUser(email: String): User? {
        return userDao.findUserByEmail(email)
    }

    override fun saldo(email: String): Flow<String?> = userDao.getSaldo(email)
}