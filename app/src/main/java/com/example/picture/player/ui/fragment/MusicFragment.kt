package com.example.picture.player.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import com.example.picture.R
import com.example.picture.base.dataBindings.DataBindingConfig
import com.example.picture.base.ui.page.BaseFragment
import com.example.picture.main.state.MainActivityViewModel
import com.example.picture.player.ui.adapter.MusicAdapter
import com.example.picture.player.ui.state.MusicViewModel
import kotlinx.android.synthetic.main.fragment_music.*

class MusicFragment: BaseFragment() {
    private lateinit var viewModel: MusicViewModel
    private lateinit var state: MainActivityViewModel
    override fun initViewModel() {
        viewModel = getFragmentScopeViewModel(MusicViewModel::class.java)
        state = getActivityScopeViewModel(MainActivityViewModel::class.java)
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_music, BR.viewModel, viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = MusicAdapter(state.musics, -1)
        recycler_view.adapter = adapter
        adapter
    }
}