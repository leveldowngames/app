package com.example.trakkamap.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExploreViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Explore tab"
    }
    val text: LiveData<String> = _text
}