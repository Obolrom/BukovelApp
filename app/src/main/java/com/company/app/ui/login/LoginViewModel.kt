package com.company.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.company.app.repository.MapRepository

class LoginViewModel(private val repository: MapRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Login Fragment"
    }
    val text: LiveData<String> = _text

    fun register(nickname: String, password: String) {
        repository.registerUser(nickname, password)
    }
}

class LoginViewModelFactory(private val repository: MapRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}