package com.example.picture.player.ui.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.picture.player.data.repository.MusicRepository

class MusicViewModel: ViewModel() {
    private var repository: MusicRepository = MusicRepository().getInstance()

    private var _snackBarMessage = MutableLiveData<Int>()
    val snackBarMessage: LiveData<Int> = _snackBarMessage
}