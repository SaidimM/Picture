package com.example.picture.player.helper

import android.media.MediaPlayer
import android.net.Uri
import com.example.picture.base.utils.Utils
import com.example.picture.player.data.bean.Mp3Info
import com.example.picture.player.util.MediaUtil


class PlayerManager {
    private var player: MediaPlayer? = null
    private var position = 0
    private var listener: PLayerStateListener? = null
    var playList: ArrayList<Mp3Info> = ArrayList()
    fun play(index: Int? = null) {
        if (player == null) {
            player = MediaPlayer()
            player!!.reset()
        }
        if(listener != null) {
            if (isPlaying())
                listener!!.onPause(position)
            else listener!!.onPlay(position)
        }
        if (playList.isEmpty()) return
        if (index != null) {
            if (index > playList.size - 1 || index < 0) return
            position = index
            player!!.reset()
            player!!.setDataSource(playList[position].url)
            player!!.prepare()
        }
        if (!player!!.isPlaying) player!!.start()
        else player!!.pause()
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
        return player!!.isPlaying
    }

    fun init(): PlayerManager {
        this.playList = MediaUtil.getMp3Infos(Utils.getApp()) as ArrayList<Mp3Info>
        this.player = MediaPlayer()
        this.player!!.reset()
        this.player!!.setOnCompletionListener {
            next()
        }
        return this
    }

    fun next(){
        position ++
        if (position == playList.size) position = 0
        play(position)
    }

    fun pre(){
        position--
        if (position < 0 ) position = playList.size - 1
        play(position)
    }

    fun destroy() {
        this.player!!.stop()
        this.player!!.release()
    }

    interface PLayerStateListener{
        fun onPause(position: Int)
        fun onPlay(position: Int)
    }

    fun setListener(listener: PLayerStateListener){
        this.listener = listener
    }
}