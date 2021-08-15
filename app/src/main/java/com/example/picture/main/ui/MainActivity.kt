package com.example.picture.main.ui

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.picture.BR
import com.example.picture.R
import com.example.picture.base.dataBindings.DataBindingConfig
import com.example.picture.base.ui.navigation.NavHostFragment
import com.example.picture.base.ui.page.BaseActivity
import com.example.picture.base.ui.page.BaseFragment
import com.example.picture.base.utils.SPUtils
import com.example.picture.main.state.MainActivityViewModel
import com.example.picture.photo.data.UnsplashPhoto
import com.example.picture.photo.ui.page.UnsplashPhotoFragment
import com.example.picture.photo.ui.service.DownloadService
import com.example.picture.player.helper.PlayerManager
import com.example.picture.player.ui.PlayerServer
import com.example.picture.player.ui.fragment.MusicFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : BaseActivity() {
    private val REQUEST_PERMISSION_CODE = 1

    private lateinit var viewModel: MainActivityViewModel

    private var cardView: CardView? = null
    private lateinit var mDrawerLayout: DrawerLayout
    private var fragments: ArrayList<BaseFragment> = ArrayList()
    private var fIndex: Int = 0
    private lateinit var downloadBinder: DownloadService.DownloadBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observe()
        PlayerManager.get().init()
        val intent = Intent(this, DownloadService::class.java)
        bindService(intent, connection, BIND_AUTO_CREATE)
    }

    fun replaceFragment(index: Int) {
        fIndex = index
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragments[index])
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private val connection: ServiceConnection = object : ServiceConnection {
        //创建一个ServiceConnection
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            //服务成功绑定时调用
            downloadBinder = iBinder as DownloadService.DownloadBinder
        }

        override fun onServiceDisconnected(componentName: ComponentName) { //断开连接时调用
        }
    }

    override fun initViewModel() {
        viewModel = getActivityScopeViewModel(MainActivityViewModel::class.java)
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_main, BR.viewModel, viewModel)
    }

    fun download(photo: UnsplashPhoto) {
        downloadBinder.startDownload(photo, object : DownloadService.DownloadStatus {
            override fun state(
                photo: UnsplashPhoto,
                msg: String,
                total: Long,
                current: Long,
                speed: Long
            ) {

            }

            override fun finish(photo: UnsplashPhoto) {
                Snackbar.make(coordinator, "completed..", 2000).setAction("check") {
                    val intent2 = Intent(Intent.ACTION_VIEW)
                    val uri = Uri.parse(
                        "file://" + Environment.getExternalStorageDirectory().absolutePath +
                                "/Download/picture/" + photo.user.name + " " + if (photo.description == null) photo.id + ".jpg" else photo.description + ".jpg"
                    )
                    intent2.setDataAndType(uri, "image/*")
                    intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent2.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    intent2.setDataAndType(uri, "application/vnd.android.package-archive")
                    startActivity(intent2)
                }.show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.main_fragment)
        return NavHostFragment.findNavController(fragment!!).navigateUp()
    }

    private fun observe() {
        viewModel.snackBarText.observe(this, {
            it.getContentIfNotHandled().let { text ->
                if (text == null) return@let
                Snackbar.make(coordinator, getString(text), Snackbar.LENGTH_LONG).setAction(
                    "点击"
                ) { Log.e("=====>>>>", "点击了啊") }.setDuration(3000).show()
            }
        })
        viewModel.openDrawer.observe(this, {
            mDrawerLayout.openDrawer(GravityCompat.START)
        })
    }

    override fun finish() {
        super.finish()
        unbindService(connection)
        downloadBinder.destroy()
        stopService(Intent(this, PlayerServer::class.java))
    }

    companion object {
        val NOTIFICATION_MUSIC = 2
    }
}