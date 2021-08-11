package com.example.picture.player.ui.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.animation.Animation
import android.widget.ProgressBar

class CustomProgressBar(context: Context, attributeSet: AttributeSet?, defStyle: Int): ProgressBar(context, attributeSet, defStyle) {
    private var progressAnimation: Animation? = null

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {

    }
    constructor(context: Context) : this(context, null){

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        
    }
}