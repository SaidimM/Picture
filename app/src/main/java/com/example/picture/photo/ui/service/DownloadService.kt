package com.example.picture.photo.ui.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.databinding.ObservableField
import com.example.picture.R
import com.example.picture.main.ui.MainActivity
import com.example.picture.photo.data.UnsplashPhoto
import com.example.picture.photo.utils.ConcurrentDownLoad
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class DownloadService : Service() {

    private val mBinder = DownloadBinder()
    private var mProgress = 0
    private var speed: Long = 0L
    private var msg: String = ""
    private var current: Long = 0L
    private var total: Long = 0L
    private lateinit var notification: Notification
    private lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1,getNotification("Downloading ... ",0));
        return super.onStartCommand(intent, flags, startId)
    }

    inner class DownloadBinder : Binder() {
        fun startDownload(photo: UnsplashPhoto) {
            doAsync {
//            DownloadMain.start(photo.urls.raw)
                ConcurrentDownLoad
                    .builder()
                    // 设置URL
                    .setUrl(photo.urls.raw)
                    // 设置线程每次请求的块大小 (5M)
                    .setBlockSize(1024L * 5)
                    // 设置线程数量
                    .setThreadCount(5)
                    // 设置保存路径
                    .setPath(
                        Environment.getExternalStorageDirectory().absolutePath +
                                "/Download/picture/" + photo.user.name + photo.description + ".jpg"
                    )
                    // 设置存在是否删除(如果设置 setKeepOnIfDisconnect(true) 则失效)
                    .setDeleteIfExist(true)
                    // 是否支持断点下载
                    .setKeepOnIfDisconnect(true)
                    // 创建
                    .build()
                    // 开始
                    .start { msg, total, current, speed ->
                        if (total == 0L) return@start
                        uiThread {
                            //构建显示下载进度的通知，并触发通知
                            getNotificationManager()!!.notify(
                                1,
                                getNotification("Downloading ...", (current / total).toInt())
                            )
                        }
                    }
            }
        }
    }

    private fun getNotificationManager(): NotificationManager? {
        return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun getNotification(title: String, progress: Int): Notification? {
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this)
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
        builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
        builder.setContentIntent(pi)
        builder.setContentTitle(title)
        if (progress >= 0) {
            //当progress大于或等0时才需要显示下载进度
            builder.setContentText("$progress%")
            builder.setProgress(100, progress, false)
        }
        return builder.build()
    }
}