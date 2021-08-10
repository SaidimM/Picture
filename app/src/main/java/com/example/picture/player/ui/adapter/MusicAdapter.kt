package com.example.picture.player.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.picture.R
import com.example.picture.databinding.ItemMusicBinding
import com.example.picture.player.data.bean.Mp3Info
import com.example.picture.player.data.bean.Music


class MusicAdapter constructor(private var musics: ArrayList<Mp3Info>, checked: Int) :
    RecyclerView.Adapter<ViewHolder>() {
    private var playPosition = checked
    private var mClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return ViewHolder(view, mClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(musics[position], playPosition)
    }

    override fun getItemCount(): Int {
        return musics.size
    }

    internal class ViewHolder(
        itemView: View,
        private val listener: OnItemClickListener?
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val mBinding: ItemMusicBinding = DataBindingUtil.bind(itemView)!!
        fun bind(music: Mp3Info, playPosition: Int) {
            mBinding.item = music
            mBinding.isPlaying = adapterPosition == playPosition
        }

        override fun onClick(view: View?) {
            if (listener == null) return
            listener.onItemClick(view, adapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, postion: Int)
    }
}