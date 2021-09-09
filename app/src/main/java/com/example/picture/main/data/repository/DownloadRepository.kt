package com.example.picture.main.data.repository

import android.provider.Settings
import com.example.picture.main.data.MyDatabase
import com.example.picture.main.data.bean.Download
import com.example.picture.main.data.dao.DownloadDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync

class DownloadRepository {
    private val dao: DownloadDao = MyDatabase.get().getDownLoadDao()

    companion object {
        private var instance: DownloadRepository? = null
            get() {
                if (field == null) field = DownloadRepository()
                return field
            }

        fun get(): DownloadRepository {
            return instance!!
        }
    }

    fun add(download: Download) {
        GlobalScope.launch {
            dao.add(download)
        }
    }

    fun update(download: Download) {
        GlobalScope.launch {
            dao.update(download)
        }
    }
}