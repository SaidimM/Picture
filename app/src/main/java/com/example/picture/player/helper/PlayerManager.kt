package com.example.picture.player.helper

import android.media.MediaPlayer
import com.example.picture.base.utils.SPUtils
import com.example.picture.base.utils.Utils
import com.example.picture.player.data.bean.Mp3Info
import com.example.picture.player.util.MediaUtil


class PlayerManager {
    var player: MediaPlayer? = null
    private var position = 0
    private var listener: PLayerStateListener? = null
    var playList: ArrayList<Mp3Info> = ArrayList()

    fun play(index: Int? = null) {
        val oldPosition = position
        if (player == null) {
            player = MediaPlayer()
            player!!.reset()
        }
        if (playList.isEmpty()) return
        if (index != null) {
            position = index
            if (oldPosition == position) {
                playOrPause()
            } else {
                if (index > playList.size - 1 || index < 0) return
                player!!.stop()
                player!!.reset()
                player!!.setDataSource(playList[position].url)
                player!!.prepare()
                player!!.start()
            }
        } else {
            playOrPause()
        }
        playList[oldPosition].isPlaying = 0
        playList[position].isPlaying = 1
        listener!!.onPlay(position, oldPosition)
    }

    private fun playOrPause() {
        if (player!!.isPlaying) {
            player!!.pause()
            if (listener != null) listener!!.onPause(position)
        } else player!!.start()
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
        this.position = SPUtils.getInstance().getInt("position", 0)
        this.player = MediaPlayer()
        this.player!!.reset()
        this.player!!.setDataSource(playList[position].url)
        this.player!!.prepare()
        this.player!!.start()
        this.player!!.pause()
        this.player!!.setOnCompletionListener { next()}
        this.player!!.setOnErrorListener { _, _, _ -> true }
        return this
    }

    fun next() {
        var new = position
        new++
        if (new == playList.size) new = 0
        play(new)
    }

    fun pre() {
        var new = position
        new--
        if (new < 0) new = playList.size - 1
        play(new)
    }

    fun destroy() {
        this.player!!.stop()
        this.player!!.release()
        this.player = null
        SPUtils.getInstance().put("position", position)
    }

    interface PLayerStateListener {
        fun onPause(position: Int)
        fun onPlay(position: Int, oldPosition: Int)
    }

    fun setListener(listener: PLayerStateListener) {
        this.listener = listener
    }
}