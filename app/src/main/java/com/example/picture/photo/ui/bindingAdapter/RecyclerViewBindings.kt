package com.example.picture.photo.ui.bindingAdapter

import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import com.example.picture.photo.ui.page.UnsplashPhotoAdapter


@BindingAdapter(value = ["adapter"], requireAll = false)
fun adapter(recyclerView: RecyclerView, adapter: UnsplashPhotoAdapter) {
    recyclerView.adapter = adapter
}

@BindingAdapter(value = ["onScroll"], requireAll = false)
fun onScroll(recyclerView: RecyclerView, constraintLayout: ConstraintLayout) {

    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        private var state: Int = 0
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            state = newState
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (state == SCROLL_STATE_DRAGGING && dy >= 50)
                constraintLayout.animate().setInterpolator(AccelerateDecelerateInterpolator())
                    .setDuration(500).alpha(0f).start()
            else if (!recyclerView.canScrollVertically(-1))
                constraintLayout.animate().setInterpolator(AccelerateDecelerateInterpolator())
                    .setDuration(500).alpha(1f).start()
        }
    })
}