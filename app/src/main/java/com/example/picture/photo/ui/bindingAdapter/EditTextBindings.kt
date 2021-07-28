package com.example.picture.photo.ui.bindingAdapter

import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.example.picture.photo.ui.state.UnsplashPickerViewModel

@BindingAdapter(value = ["bindSearch"], requireAll = false)
fun bindSearch(editText: EditText, viewModel: UnsplashPickerViewModel){
    viewModel.bindSearch(editText)
}