package com.company.app.ui.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.company.app.repository.MapRepository

class ServiceViewModel(private val repository: MapRepository) : ViewModel() {

    fun getServices(): LiveData<List<Service>> {
        return repository.getServices()
    }

    fun getServiceReviews(serviceName: String) : MutableLiveData<List<ServiceReview>> {
        return repository.getServiceReviews(serviceName)
    }
}

class ServiceViewModelFactory(private val repository: MapRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServiceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}