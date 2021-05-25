package com.company.app.ui.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val currentServiceName: MutableLiveData<String> = MutableLiveData<String>()
}