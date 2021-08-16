package com.example.picture.main.data

import android.os.Parcel
import android.os.Parcelable

data class DownloadBean(var id: String, var type: Int, var link: String, var state: Int, var path: String) : Parcelable{
    val PHOTO = 1
    val MUSIC= 2
    val VIDEO = 3

    val COMPLETE = 1
    val ERROR = 2
    val STOPPED = 3

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(type)
        parcel.writeString(link)
        parcel.writeInt(state)
        parcel.writeString(path)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DownloadBean> {
        override fun createFromParcel(parcel: Parcel): DownloadBean {
            return DownloadBean(parcel)
        }

        override fun newArray(size: Int): Array<DownloadBean?> {
            return arrayOfNulls(size)
        }
    }
}