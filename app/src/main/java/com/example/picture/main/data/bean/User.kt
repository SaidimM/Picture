package com.example.picture.main.data.bean

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user")
class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "name")
    var name = ""

    @ColumnInfo(name = "pass")
    var pass = ""

    @ColumnInfo(name = "p_number")
    var pNumber = ""

    @ColumnInfo(name = "email")
    var email = ""

    @ColumnInfo(name = "gender")
    var gender = ""

    @ColumnInfo(name = "is_member")
    var isMember = false

    @ColumnInfo(name = "last_logged")
    var lastLogged = ""

    @ColumnInfo(name = "registered_when")
    var registeredWhen = ""
}