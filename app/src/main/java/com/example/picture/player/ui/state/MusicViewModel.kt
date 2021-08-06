package com.example.picture.player.ui.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MusicViewModel: ViewModel() {

    private var _snackBarMessage = MutableLiveData<Int>()
    val snackBarMessage: LiveData<Int> = _snackBarMessage
}