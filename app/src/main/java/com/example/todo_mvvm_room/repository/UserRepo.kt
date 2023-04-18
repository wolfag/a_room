package com.example.todo_mvvm_room.repository

import androidx.lifecycle.LiveData
import com.example.todo_mvvm_room.data.UserDao
import com.example.todo_mvvm_room.model.User

class UserRepo(private val userDao: UserDao) {
    val fetchAll: LiveData<List<User>> = userDao.fetchAll()

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    suspend fun delete(user: User) {
        userDao.delete(user)
    }

    suspend fun deleteAll() {
        userDao.deleteAll()
    }

    fun searchAll(query: String): LiveData<List<User>> {
        return userDao.search(query)
    }
}