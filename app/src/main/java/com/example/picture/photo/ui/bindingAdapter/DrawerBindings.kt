package com.example.picture.photo.ui.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.example.picture.main.state.MainActivityViewModel
import com.example.picture.photo.ui.state.UnsplashPickerViewModel

@BindingAdapter(value = ["openDrawer"], requireAll = false)
fun openDrawer(imageView: ImageView, viewModel: MainActivityViewModel) {
    imageView.setOnClickListener { viewModel.openDrawer() }
}