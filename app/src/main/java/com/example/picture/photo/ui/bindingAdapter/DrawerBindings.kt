package com.example.picture.photo.ui.bindingAdapter

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import com.example.picture.main.state.MainActivityViewModel

@BindingAdapter(value = ["openDrawer"], requireAll = false)
fun openDrawer(imageView: ImageView, viewModel: MainActivityViewModel) {
    imageView.setOnClickListener { viewModel.openDrawer() }
}

@BindingAdapter(value = ["addDrawerListener"], requireAll = false)
fun addDrawerListener(drawerLayout: DrawerLayout, cardView: CardView){
    drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            val mContent = drawerLayout.getChildAt(0)
            val scale = 1 - slideOffset
            val rightScale = 0.8f + scale * 0.2f
            val leftScale = 0.5f + slideOffset * 0.5f
            drawerView.alpha = leftScale
            drawerView.scaleX = leftScale
            drawerView.scaleY = leftScale
            mContent.pivotX = 0f
            mContent.pivotY = (mContent.height / 2).toFloat()
            mContent.scaleX = rightScale
            mContent.scaleY = rightScale
            mContent.translationX = drawerView.width * slideOffset
        }

        override fun onDrawerOpened(drawerView: View) {
            cardView.radius = 20f
        }

        override fun onDrawerClosed(drawerView: View) {
            cardView.radius = 0f
        }

        override fun onDrawerStateChanged(newState: Int) {}
    })
}

@BindingAdapter(value = ["scrimColor"], requireAll = false)
fun scrimColor(drawerLayout: DrawerLayout, color: Int){
    drawerLayout.setScrimColor(color)
}