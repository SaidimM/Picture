package com.example.picture.player.data.repository

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.core.net.toUri
import com.example.picture.R
import com.example.picture.base.utils.Utils
import com.example.picture.player.data.bean.Music
import com.example.picture.player.manager.MusicManager
import org.jetbrains.anko.doAsync
import java.io.FileNotFoundException

class MusicRepository {
    private var instance: MusicRepository? = null
    fun getInstance(): MusicRepository {
        if (instance == null) instance = MusicRepository()
        return instance!!
    }

    fun getMusicList(): ArrayList<Music> {
        var musics: ArrayList<Music> = ArrayList()
        doAsync {
            musics = MusicManager.getInstance(Utils.getApp()).musics
        }
        return musics
    }

    fun getAlbumArtBitmap(context: Context, albumId: Long?): Bitmap? {
        if (albumId == null) return null
        return try {
            MediaStore.Images.Media.getBitmap(context.contentResolver, getAlbumArtUri(albumId))
        } catch (e: FileNotFoundException) {
            BitmapFactory.decodeResource(context.resources, R.mipmap.album_cover)
        }
    }

    private fun getAlbumArtUri(albumId: Long) = ContentUris.withAppendedId(
        "content://media/external/audio/albumart".toUri(),
        albumId
    )

}