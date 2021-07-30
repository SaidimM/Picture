package com.example.picture.photo.ui.page

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.picture.BR
import com.example.picture.R
import com.example.picture.base.dataBindings.DataBindingConfig
import com.example.picture.base.ui.page.BaseFragment
import com.example.picture.main.state.MainActivityViewModel
import com.example.picture.main.ui.MainActivity
import com.example.picture.photo.Injector
import com.example.picture.photo.data.UnsplashPhoto
import com.example.picture.photo.ui.service.DownloadService
import com.example.picture.photo.ui.state.UnsplashPickerViewModel
import kotlinx.android.synthetic.main.fragment_unsplash.*


class UnsplashPhotoFragment : BaseFragment() {
    private lateinit var viewModel: UnsplashPickerViewModel
    private lateinit var state: MainActivityViewModel
    private lateinit var adapter: UnsplashPhotoAdapter
    private lateinit var notificationManager: NotificationManager
    private lateinit var downloadBinder: DownloadService.DownloadBinder
    override fun initViewModel() {
        state = getActivityScopeViewModel(MainActivityViewModel::class.java)
        viewModel = ViewModelProviders.of(this, Injector.createPickerViewModelFactory())
            .get(UnsplashPickerViewModel::class.java)
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_unsplash, BR.viewModel, viewModel)
            .addBindingParam(BR.adapter, adapter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = UnsplashPhotoAdapter(requireContext(), false, viewModel)
        observeViewModel()
    }

    /**
     * Observes the live data in the view model.
     */
    private fun observeViewModel() {
        viewModel.errorLiveData.observe(this, {
            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
        })
        viewModel.messageLiveData.observe(this, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.loadingLiveData.observe(this, {
            unsplash_picker_progress_bar_layout.visibility =
                if (it != null && it) View.VISIBLE else View.GONE
        })
        viewModel.photosLiveData.observe(this, {
            adapter.submitList(it)
        })
        viewModel.openDrawer.observe(this, {
            (context as MainActivity).openDrawer()

        })
    }
}