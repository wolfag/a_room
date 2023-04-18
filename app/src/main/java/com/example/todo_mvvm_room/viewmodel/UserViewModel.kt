package com.example.todo_mvvm_room.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todo_mvvm_room.data.UserDatabase
import com.example.todo_mvvm_room.repository.UserRepo
import com.example.todo_mvvm_room.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    val fetchAll: LiveData<List<User>>
    private val repo: UserRepo

    init {
        val userDao = UserDatabase.getInstance(application).userDao()

        repo = UserRepo(userDao)
        fetchAll = repo.fetchAll
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.update(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.delete(user)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAll()
        }
    }

    fun searchAll(query: String): LiveData<List<User>> {
        return repo.searchAll(query)
    }
}