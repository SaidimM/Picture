package com.example.picture.main.data.bean

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

/**
 * download bean
 *
 * PHOTO for type 1
 * VIDEO for type 2
 * AUDIO for type 3
 *
 * PREPARING for state 1
 * DOWNLOADING for state 2
 * FINISHED for state 3
 * STOPPED for state 4
 * FINISHED for state 5
 */
@Entity(tableName = "download")
data class Download(
        @ColumnInfo(name = "type")
        var type: Int = 0,

        @ColumnInfo(name = "link")
        var link: String = "",

        @ColumnInfo(name = "state")
        var state: Int = 0,

        @ColumnInfo(name = "path")
        var path: String = "",

        @ColumnInfo(name = "user_id")
        var userID: Int = 0,

        @ColumnInfo(name = "file_id")
        var fileID: String = "",

        @ColumnInfo(name = "created_time")
        var createdTime: String = ""
) : Serializable{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}