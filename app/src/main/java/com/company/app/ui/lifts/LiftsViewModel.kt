package com.company.app.ui.lifts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.company.app.repository.Repository
import com.company.app.ui.map.Lift
import com.company.app.ui.services.ServiceViewModel

class LiftsViewModel(private val repository: Repository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
    val lifts: LiveData<List<Lift>> = repository.lifts
}

class LiftsViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LiftsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LiftsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}