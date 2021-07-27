package com.example.picture.base

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

open class BaseApplication: Application(), ViewModelStoreOwner {

    private lateinit var mViewModelStore: ViewModelStore
    override fun getViewModelStore(): ViewModelStore {
        return mViewModelStore
    }

    override fun onCreate() {
        super.onCreate()
        mViewModelStore = ViewModelStore()
    }
}