package com.example.picture.photo.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.picture.photo.domain.Repository

/**
 * View model factory for the photo screen.
 * This will use the repository to create the view model.
 */
class UnsplashPickerViewModelFactory constructor(private val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UnsplashPickerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UnsplashPickerViewModel(repository) as T
        } else {
            throw IllegalArgumentException("unknown model class $modelClass")
        }
    }
}
