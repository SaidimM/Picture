package com.example.picture.main.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.picture.BR
import com.example.picture.R
import com.example.picture.base.dataBindings.DataBindingConfig
import com.example.picture.base.ui.page.BaseActivity
import com.example.picture.main.state.MainActivityViewModel
import com.example.picture.photo.data.UnsplashPhoto
import com.example.picture.photo.ui.service.DownloadService
import com.example.picture.player.helper.PlayerManager
import com.example.picture.player.ui.PlayerServer
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import java.util.*


class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var mDrawerLayout: DrawerLayout
    lateinit var downloadBinder: DownloadService.DownloadBinder
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observe()
        setupNavigationDrawer()
        val naviController: NavController = (supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment).navController
        appBarConfiguration = AppBarConfiguration.Builder(R.id.main_fragment, R.id.music_fragment).setDrawerLayout(mDrawerLayout).build()
        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(naviController)
        PlayerManager.get().init()
        val intent = Intent(this, DownloadService::class.java)
        bindService(intent, connection, BIND_AUTO_CREATE)
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

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_main, BR.viewModel, viewModel)
    }

    override fun initViewModel() {
        viewModel = getActivityScopeViewModel(MainActivityViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.main_fragment)
        return NavHostFragment.findNavController(fragment!!).navigateUp()
    }

    private fun setupNavigationDrawer() {
        mDrawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout))
                .apply {
                    setStatusBarBackground(R.color.transparent)
                }
    }

    private fun observe() {
        viewModel.snackBarText.observe(this) {
            it.getContentIfNotHandled().let { text ->
                if (text == null) return@let
                Snackbar.make(coordinator, getString(text), Snackbar.LENGTH_LONG).setAction(
                        "点击"
                ) { Log.e("=====>>>>", "点击了啊") }.setDuration(3000).show()
            }
        }
        viewModel.openDrawer.observe(this) {
            mDrawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun finish() {
        super.finish()
        unbindService(connection)
        downloadBinder.destroy()
        stopService(Intent(this, PlayerServer::class.java))
    }

    companion object {
        val NOTIFICATION_DOWNLOAD = 1
        val NOTIFICATION_MUSIC = 2
        val PICTURE_DOWNLOAD_PATH = Environment.getExternalStorageDirectory().absolutePath +
                "/Download/picture/"
    }
}