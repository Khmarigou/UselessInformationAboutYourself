package com.example.uselessinformationaboutyourself.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uselessinformationaboutyourself.data.AppDatabase
import com.example.uselessinformationaboutyourself.data.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).userDao()
    val user = dao.getUser().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun saveUser(name: String, birthDate: String, height: Int) {
        viewModelScope.launch {
            dao.insert(User(name = name, birthDate = birthDate, height = height))
        }
    }
}