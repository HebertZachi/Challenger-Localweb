package br.com.fiap.challengerlocalweb.repository

import br.com.fiap.challengerlocalweb.dao.UserDao
import br.com.fiap.challengerlocalweb.model.User

class UserRepository(private val userDao: UserDao) {

    suspend fun insertOrUpdateUser(user: User) {
        userDao.insertOrUpdateUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}



