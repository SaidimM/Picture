package com.example.picture.main.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.picture.main.Event
import com.example.picture.player.helper.PlayerManager

class MainActivityViewModel : ViewModel() {

    private val _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarText: LiveData<Event<Int>> = _snackBarText
}