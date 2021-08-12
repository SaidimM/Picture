package com.example.picture.main.ui

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.FragmentManager
import com.example.picture.BR
import com.example.picture.R
import com.example.picture.base.dataBindings.DataBindingConfig
import com.example.picture.base.ui.page.BaseActivity
import com.example.picture.base.ui.page.BaseFragment
import com.example.picture.main.state.MainActivityViewModel
import com.example.picture.photo.data.UnsplashPhoto
import com.example.picture.photo.ui.page.UnsplashPhotoFragment
import com.example.picture.photo.ui.service.DownloadService
import com.example.picture.player.ui.fragment.MusicFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.EditText

import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.example.picture.player.helper.PlayerManager
import com.example.picture.player.ui.PlayerServer
import androidx.core.content.ContextCompat
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.example.picture.wakeup.inputstream.InFileStream

import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : BaseActivity(), EventListener  {
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val REQUEST_PERMISSION_CODE = 1

    private lateinit var viewModel: MainActivityViewModel

    private var cardView: CardView? = null
    private lateinit var mDrawerLayout: DrawerLayout
    private var fragments: ArrayList<BaseFragment> = ArrayList()
    private var fIndex: Int = 0
    private lateinit var downloadBinder: DownloadService.DownloadBinder
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var wakeup: EventManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
        }
        setContentView(R.layout.activity_main)
        initView()
        initPermission()
        // 基于SDK唤醒词集成1.1 初始化EventManager
        wakeup = EventManagerFactory.create(this, "wp");
        // 基于SDK唤醒词集成1.3 注册输出事件
        wakeup.registerListener(this); //  EventListener 中 onEvent方法
        start()
        observe()
        PlayerManager.get().init()
        val intent = Intent(this, DownloadService::class.java)
        bindService(intent, connection, BIND_AUTO_CREATE)
    }

    private fun initView() {
        mediaPlayer = MediaPlayer()
        fragments.add(UnsplashPhotoFragment())
        fragments.add(MusicFragment())
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mDrawerLayout.setScrimColor(Color.TRANSPARENT)
        cardView = findViewById(R.id.card_view)
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
        initMap()
        replaceFragment(0)
    }

    private fun initMap() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val mapFragment = MenuFragment()
        fragmentManager.beginTransaction().replace(R.id.nav_view, mapFragment).commit()
    }

    fun replaceFragment(index: Int) {
        fIndex = index
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragments[index])
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (i in permissions.indices) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i])
            }
        }
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
                                "/Download/picture/" + photo.user.name + " " + photo.id + ".jpg"
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

    /**
     * android 6.0 以上需要动态申请权限
     */
    private fun initPermission() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val toApplyList = ArrayList<String>()
        for (perm in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                    this,
                    perm
                )
            ) {
                toApplyList.add(perm)
                // 进入到这里代表没有权限.
            }
        }
        val tmpList = arrayOfNulls<String>(toApplyList.size)
        if (toApplyList.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123)
        }
    }

    override fun finish() {
        super.finish()
        unbindService(connection)
        downloadBinder.destroy()
        stopService(Intent(this, PlayerServer::class.java))
    }

    companion object{
        val NOTIFICATION_MUSIC = 2
    }

    //  基于SDK唤醒词集成1.2 自定义输出事件类 EventListener  回调方法
    // 基于SDK唤醒3.1 开始回调事件
    override fun onEvent(
        name: String,
        params: String?,
        data: ByteArray?,
        offset: Int,
        length: Int
    ) {
        var logTxt = "name: $name"
        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :$params"
        } else if (data != null) {
            logTxt += " ;data length=" + data.size
        }
        printLog(logTxt)
    }

    private fun printLog(text1: String) {
        var text = text1
        if (logTime) {
            text += "  ;time=" + System.currentTimeMillis()
        }
        text += "\n"
        Log.i(javaClass.name, text)
    }

    private val logTime = true

    /**
     * 测试参数填在这里
     * 基于SDK唤醒词集成第2.1 设置唤醒的输入参数
     */
    private fun start() {
        // 基于SDK唤醒词集成第2.1 设置唤醒的输入参数
        val params: MutableMap<String?, Any?> = TreeMap<String?, Any?>()
        params[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        params[SpeechConstant.WP_WORDS_FILE] = "assets:///WakeUp.bin"
        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        InFileStream.setContext(this)
        var json: String? = null // 这里可以替换成你需要测试的json
        json = JSONObject(params).toString()
        wakeup.send(SpeechConstant.WAKEUP_START, json, null, 0, 0)
        print("输入参数：$json")
    }

    // 基于SDK唤醒词集成第4.1 发送停止事件
    private fun stop() {
        wakeup.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0) //
    }
}