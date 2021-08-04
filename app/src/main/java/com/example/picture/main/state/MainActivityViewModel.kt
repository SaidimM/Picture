package com.example.picture.main.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.picture.main.Event
import com.example.picture.player.data.bean.Music
import com.example.picture.player.data.repository.MusicRepository

class MainActivityViewModel: ViewModel() {
    var musics = MusicRepository().getMusicList()

    private val _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarText: LiveData<Event<Int>> = _snackBarText

    private var _position = MutableLiveData<Int>()
    private val position: LiveData<Int> = _position
}