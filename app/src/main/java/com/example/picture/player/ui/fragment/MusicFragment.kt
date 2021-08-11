package com.example.picture.player.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import com.example.picture.R
import com.example.picture.base.dataBindings.DataBindingConfig
import com.example.picture.base.ui.page.BaseFragment
import com.example.picture.main.state.MainActivityViewModel
import com.example.picture.photo.ui.bindingAdapter.adapter
import com.example.picture.player.helper.PlayerManager
import com.example.picture.player.ui.PlayerServer
import com.example.picture.player.ui.adapter.MusicAdapter
import com.example.picture.player.ui.state.MusicViewModel
import kotlinx.android.synthetic.main.fragment_music.*
import okhttp3.internal.notify

class MusicFragment: BaseFragment() {
    private lateinit var viewModel: MusicViewModel
    private lateinit var state: MainActivityViewModel
    private lateinit var adapter: MusicAdapter
    override fun initViewModel() {
        viewModel = getFragmentScopeViewModel(MusicViewModel::class.java)
        state = getActivityScopeViewModel(MainActivityViewModel::class.java)
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_music, BR.viewModel, viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        observe()
        adapter = MusicAdapter(viewModel.musics)
        recycler_view.adapter = adapter
        adapter.setOnItemClickListener(object: MusicAdapter.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int) {
                viewModel.setPosition(position)
                context?.startService(Intent(context, PlayerServer::class.java))
            }
        })
    }

    private fun setListener(){
        viewModel.manager.setListener(listener)
    }

    private fun observe(){
        viewModel.position.observe(mActivity, {
            viewModel.manager.play(it)
        })
    }

    private val listener= object: PlayerManager.PLayerStateListener{
        override fun onPause(position: Int) {
            adapter.notifyItemChanged(position)
            context?.startService(Intent(context, PlayerServer::class.java))
        }

        override fun onPlay(position: Int, oldPosition: Int) {
            adapter.notifyItemChanged(position)
            if (position != oldPosition) adapter.notifyItemChanged(oldPosition)
        }
    }
}