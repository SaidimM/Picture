package com.example.picture.main.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.picture.main.Event
import com.example.picture.photo.data.UnsplashPhoto

class MainActivityViewModel: ViewModel() {


    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText
}