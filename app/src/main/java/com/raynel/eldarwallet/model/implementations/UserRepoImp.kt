package com.raynel.eldarwallet.model.implementations

import com.raynel.eldarwallet.model.db.entities.User
import com.raynel.eldarwallet.model.db.daos.UserDao
import com.raynel.eldarwallet.model.interfaces.UserRepo
import kotlinx.coroutines.flow.Flow

class UserRepoImp(private val userDao: UserDao): UserRepo {
    override suspend fun getUser(email: String): User? {
        return userDao.findUserByEmail(email)
    }

    override fun amount(email: String): Flow<String?> = userDao.getSaldo(email)
}