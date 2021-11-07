package com.exchangerates.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exchangerates.model.Exchange

class MainViewModel: ViewModel() {
    var exchangeList: MutableLiveData<List<Exchange>> = MutableLiveData<List<Exchange>>()
}