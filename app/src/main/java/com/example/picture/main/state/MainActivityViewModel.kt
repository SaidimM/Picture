package com.example.picture.main.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.picture.photo.data.UnsplashPhoto

class MainActivityViewModel: ViewModel() {
    private var _downloadUrl = MutableLiveData("")
    val downloadUrl: LiveData<String> = _downloadUrl

    fun download(photo: UnsplashPhoto){
        _downloadUrl.value = photo.urls.raw
    }
}