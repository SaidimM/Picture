package com.example.picture.main.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.picture.main.data.bean.Download
import com.example.picture.main.data.bean.User
import com.example.picture.main.data.dao.DownloadDao

@Database(entities = [Download::class],version = 1)
abstract class MyDatabase: RoomDatabase() {
    abstract fun getDownLoadDao(): DownloadDao
}