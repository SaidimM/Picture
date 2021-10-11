package com.example.picture.main.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.picture.base.utils.Utils
import com.example.picture.main.data.bean.Download
import com.example.picture.main.data.dao.DownloadDao

@Database(entities = [Download::class], version = 1)
abstract class MyDatabase: RoomDatabase() {
    abstract fun getDownLoadDao(): DownloadDao
    companion object{
        private var instance: MyDatabase? = null
        get() {
            if (field == null) field =
                    Room.databaseBuilder(
                            Utils.getApp(),
                            MyDatabase::class.java,
                            "picture"
                    ).build()
            return field
        }
        fun get(): MyDatabase{
            return instance!!
        }
    }
}