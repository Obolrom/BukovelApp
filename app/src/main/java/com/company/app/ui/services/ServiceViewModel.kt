package com.company.app.ui.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.company.app.repository.Repository

class ServiceViewModel(private val repository: Repository) : ViewModel() {

    // FIXME: 13.04.21 add repository
    val services = MutableLiveData<List<Service>>().apply {
        value = listOf(
            Service(3.5f, "Ski school", 120932),
            Service(4.6f, "Хаски покатушки", 239012),
            Service(4.7f, "Whisky Bar", 120932),
            Service(3.89f, "Lords of the Boards", 239012),
            Service(2.5f, "Ski school", 120932),
            Service(2.6f, "Хаски покатушки", 239012),
            Service(2.7f, "Whisky Bar", 120932),
            Service(3.89f, "Lords of the Boards", 239012),
            Service(1.5f, "Ski school", 120932),
            Service(1.6f, "Хаски покатушки", 239012),
            Service(1.7f, "Whisky Bar", 120932),
            Service(1.89f, "Lords of the Boards", 239012)
        )
    }
}

class ServiceViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServiceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}