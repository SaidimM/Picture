package com.example.picture.main.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.picture.main.Event
import com.example.picture.player.helper.PlayerManager

class MainActivityViewModel : ViewModel() {
    var manager: PlayerManager = PlayerManager.get().init()
    var musics = PlayerManager.get().playList

    private val _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarText: LiveData<Event<Int>> = _snackBarText

    private var _position = MutableLiveData(0)
    val position: LiveData<Int> = _position

    fun nextMusic() {
        if (_position.value == null) _position.value = 0
        if (_position.value!! >= musics.size) _position.value = 0
        else _position.value = _position.value!! + 1
    }

    fun preMusic() {
        if (_position.value == null) _position.value = 0
        if (_position.value!! <= 0) _position.value = musics.size - 1
        else _position.value = _position.value!! - 1
    }

    fun play() {
        manager.play()
    }
}