package com.example.picture.player.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.picture.R
import com.example.picture.databinding.ItemMusicBinding
import com.example.picture.player.data.bean.Music


class MusicAdapter constructor(private var musics: ArrayList<Music>, checked: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var playPosition = checked
    private var mMusics = musics
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(musics[position], playPosition)
    }

    override fun getItemCount(): Int {
        return musics.size
    }

    internal class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    ) {
        private val mBinding: ItemMusicBinding = DataBindingUtil.bind(itemView!!)!!
        fun bind(music: Music, playPosition: Int) {
            mBinding.item = music
            mBinding.isPlaying = position == playPosition
        }
    }
}