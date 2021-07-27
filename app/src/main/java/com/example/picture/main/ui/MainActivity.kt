package com.example.picture.main.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import com.example.picture.R
import com.example.picture.base.ui.page.BaseFragment
import com.example.picture.photo.ui.page.UnsplashPhotoFragment

class MainActivity : AppCompatActivity(){
    private val cardView: CardView? = null
    private lateinit var mDrawerLayout: DrawerLayout
    private var fragments: ArrayList<BaseFragment> = ArrayList()
    private var fIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView(){
        fragments.add(UnsplashPhotoFragment())
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mDrawerLayout.setScrimColor(Color.TRANSPARENT)
        mDrawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                val mContent = mDrawerLayout.getChildAt(0)
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
                cardView!!.radius = 20f
            }

            override fun onDrawerClosed(drawerView: View) {
                cardView!!.radius = 0f
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })
        replaceFragment(0)
    }
    fun replaceFragment(index: Int) {
        fIndex = index
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragments[index])
        transaction.addToBackStack(null)
        transaction.commit()
    }
}