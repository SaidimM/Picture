package com.example.picture.player.ui.state

import android.widget.SeekBar
import androidx.databinding.BindingAdapter
import com.example.picture.player.data.bean.Mp3Info
import com.example.picture.player.helper.PlayerManager
import java.io.IOException
import java.util.*

@BindingAdapter(value = ["setProgress"], requireAll = false)
fun setProgress(seekBar: SeekBar, item: Mp3Info){
    if (item.isPlaying == 1){
        seekBar.max = item.duration
        val timer = Timer()
        timer.schedule(object: TimerTask(){
            override fun run() {
                try {
                    seekBar.progress = PlayerManager.get().player!!.currentPosition
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        }, 0, 500)
    }
}