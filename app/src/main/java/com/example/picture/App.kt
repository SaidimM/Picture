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
            "Cr8mPVWJ9Rx8TjyKMVK9VyIStA9KAKRnr7PApzTtFRI",
            "svdtbTCsgdYbtxGtWMq8RGnXEgN0mjebxhHeoiozxnU"
            /* optional page size (number of photos per page) */
        )
    }
}