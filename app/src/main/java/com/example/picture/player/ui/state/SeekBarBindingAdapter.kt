package com.example.picture.player.ui.state

import android.view.View
import android.widget.AbsSeekBar
import android.widget.SeekBar
import androidx.databinding.BindingAdapter
import com.example.picture.player.data.bean.Mp3Info
import com.example.picture.player.helper.PlayerManager
import java.io.IOException
import java.util.*

@BindingAdapter(value = ["setProgress"], requireAll = false)
fun setProgress(seekBar: SeekBar, item: Mp3Info){
    if (seekBar.visibility == View.VISIBLE){
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