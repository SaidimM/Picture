package com.example.picture

import com.example.picture.base.BaseApplication
import com.example.picture.base.utils.Utils
import com.example.picture.photo.UnsplashPhotoPicker

class App: BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        UnsplashPhotoPicker.init(
            this,
            "your access key",
            "your secret key"
            /* optional page size (number of photos per page) */
        )
    }
}