package com.example.trakkamap.ui.achievements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AchievementsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Achievements tab"
    }
    val text: LiveData<String> = _text
}