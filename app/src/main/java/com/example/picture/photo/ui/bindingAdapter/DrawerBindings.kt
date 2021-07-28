package com.example.picture.photo.ui.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.picture.photo.ui.state.UnsplashPickerViewModel

@BindingAdapter(value = ["openDrawer"], requireAll = false)
fun openDrawer(imageView: ImageView, viewModel: UnsplashPickerViewModel) {
    imageView.setOnClickListener { viewModel.openDrawer() }
}