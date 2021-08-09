package com.example.picture.player.helper

import android.media.MediaPlayer
import android.net.Uri
import com.example.picture.base.utils.Utils
import com.example.picture.player.data.bean.Mp3Info
import com.example.picture.player.util.MediaUtil


class PlayerManager {
    private lateinit var player: MediaPlayer
    private var position = 0
    var playList: ArrayList<Mp3Info> = ArrayList()
    fun play(index: Int? = null) {
        if (playList.isEmpty()) return
        if (index != null) {
            if (index > playList.size - 1 || index < 0) return
            position = index
            player.setDataSource(Utils.getApp(), Uri.parse(playList[position].url))
        }
        if (!player.isPlaying) player.start()
        else player.stop()
    }

    fun getCurrentMusic(): Mp3Info {
        return playList[position]
    }

    companion object {
        private var instance: PlayerManager? = null
            get() {
                if (field == null) field = PlayerManager()
                return field
            }

        @Synchronized
        fun get(): PlayerManager {
            return instance!!
        }
    }

    fun isPlaying(): Boolean {
        return player.isPlaying
    }

    fun init(): PlayerManager {
        this.playList = MediaUtil.getMp3Infos(Utils.getApp()) as ArrayList<Mp3Info>
        this.player = MediaPlayer()
        return this
    }
}