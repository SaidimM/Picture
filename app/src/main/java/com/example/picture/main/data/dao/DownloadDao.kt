package com.example.picture.main.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.example.picture.main.data.bean.Download

@Dao
interface DownloadDao {
    @Insert
    fun add(download: Download)

    @Update
    fun update(download: Download)
}