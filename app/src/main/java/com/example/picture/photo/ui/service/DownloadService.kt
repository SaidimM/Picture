package com.example.picture.photo.ui.service

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.Environment
import android.os.IBinder
import com.example.picture.R
import com.example.picture.photo.data.UnsplashPhoto
import com.example.picture.photo.utils.ConcurrentDownLoad
import org.jetbrains.anko.doAsync


class DownloadService : Service() {

    private val mBinder = DownloadBinder()
    private val ID = "com.example.picture"
    private val NAME = "channel one"
    private lateinit var downloadStatus: DownloadStatus

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    inner class DownloadBinder : Binder() {
        fun startDownload(photo: UnsplashPhoto, downloadStatus: DownloadStatus) {
            this@DownloadService.downloadStatus = downloadStatus
            val fileName = photo.user.name + " " + if (photo.description == null) photo.id else photo.description + ".jpg"
            doAsync {
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
                                "/Download/picture/" + fileName
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
                        getNotification((current * 100 / total).toInt())
                        downloadStatus.state(photo, msg, total, current, speed)
                        if (total == current) {
                            getNotificationManager().cancel(1)
                            downloadStatus.finish(photo)
                        }
                    }
            }
        }

        fun destroy() {
            this@DownloadService.onDestroy()
        }
    }

    private fun getNotificationManager(): NotificationManager {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_LOW)
        manager.createNotificationChannel(channel)
        return manager
    }

    private fun getNotification(progress: Int): Notification {
        val notificationBuilder = Notification.Builder(
            this@DownloadService, ID
        ) //设置通知左侧的小图标
            .setSmallIcon(R.drawable.ic_download) //设置通知标题
            .setContentTitle("downloading...") //设置通知内容
            .setContentText("$progress%") //设置通知不可删除
            .setOngoing(true) //设置显示通知时间
            .setShowWhen(true) //设置点击通知时的响应事件
            .setProgress(100, progress, false)
        val notification = notificationBuilder.build()
        getNotificationManager().notify(1, notification)
        return notification
    }



    interface DownloadStatus {
        fun state(photo: UnsplashPhoto, msg: String, total: Long, current: Long, speed: Long)
        fun finish(photo: UnsplashPhoto)
    }
}