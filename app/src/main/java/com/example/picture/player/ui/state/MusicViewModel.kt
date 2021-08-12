package com.example.picture.player.ui.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.picture.player.helper.PlayerManager

class MusicViewModel: ViewModel() {
    var manager: PlayerManager = PlayerManager.get()
    var musics = PlayerManager.get().playList

    private var _snackBarMessage = MutableLiveData<Int>()
    val snackBarMessage: LiveData<Int> = _snackBarMessage

    private var _position = MutableLiveData<Int>()
    val position: LiveData<Int> = _position

    fun setPosition(p: Int){
        _position.value = p
    }
}