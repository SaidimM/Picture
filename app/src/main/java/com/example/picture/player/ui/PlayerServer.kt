package com.example.picture.player.ui

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.widget.RemoteViews
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModel
import com.example.picture.R
import com.example.picture.base.utils.ImageUtils
import com.example.picture.main.state.MainActivityViewModel
import com.example.picture.main.ui.MainActivity
import com.example.picture.player.helper.PlayerManager
import com.example.picture.player.util.MediaUtil

class PlayerServer: Service() {
    private val ID = "com.example.picture.player.ui"
    private val NAME = "channel two"
    private lateinit var playerManager: PlayerManager
    val ACTION_SIMPLE = "com.example.picture.player.ui.ACTION_SIMPLE";
    val ACTION_ACTION = "com.example.picture.player.ui.ACTION_ACTION";
    val ACTION_REMOTE_INPUT = "com.example.picture.player.ui.ACTION_REMOTE_INPUT";
    val ACTION_BIG_PICTURE_STYLE = "com.example.picture.player.ui.ACTION_BIG_PICTURE_STYLE";
    val ACTION_BIG_TEXT_STYLE = "com.example.picture.player.ui.ACTION_BIG_TEXT_STYLE";
    val ACTION_INBOX_STYLE = "com.example.picture.player.ui.ACTION_INBOX_STYLE";
    val ACTION_MEDIA_STYLE = "com.example.picture.player.ui.ACTION_MEDIA_STYLE";
    val ACTION_MESSAGING_STYLE = "com.example.picture.player.ui.ACTION_MESSAGING_STYLE";
    val ACTION_PROGRESS = "com.example.picture.player.ui.ACTION_PROGRESS";
    val ACTION_CUSTOM_HEADS_UP_VIEW =
        "com.example.picture.player.ui.ACTION_CUSTOM_HEADS_UP_VIEW";
    val ACTION_CUSTOM_VIEW = "com.example.picture.player.ui.ACTION_CUSTOM_VIEW";
    val ACTION_CUSTOM_VIEW_OPTIONS_LOVE =
        "com.example.picture.player.ui.ACTION_CUSTOM_VIEW_OPTIONS_LOVE";
    val ACTION_CUSTOM_VIEW_OPTIONS_PRE =
        "com.example.picture.player.ui.ACTION_CUSTOM_VIEW_OPTIONS_PRE";
    val ACTION_CUSTOM_VIEW_OPTIONS_PLAY_OR_PAUSE =
        "com.example.picture.player.ui.ACTION_CUSTOM_VIEW_OPTIONS_PLAY_OR_PAUSE";
    val ACTION_CUSTOM_VIEW_OPTIONS_NEXT =
        "com.example.picture.player.ui.ACTION_CUSTOM_VIEW_OPTIONS_NEXT";
    val ACTION_CUSTOM_VIEW_OPTIONS_LYRICS =
        "com.example.picture.player.ui.ACTION_CUSTOM_VIEW_OPTIONS_LYRICS";
    val ACTION_CUSTOM_VIEW_OPTIONS_CANCEL = "com.example.picture.player.ui.ACTION_CUSTOM_VIEW_OPTIONS_CANCEL";
    val ACTION_CUSTOM_VIEW_OPTIONS_FORWARD = "com.example.pic.ui.ACTION_CUSTOM_VIEW_OPTIONS_FORWARD";

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        playerManager = PlayerManager.get()
        when(intent?.action){
            ACTION_CUSTOM_VIEW_OPTIONS_CANCEL -> {
                this.stopService(intent)
                getNotificationManager().cancel(MainActivity.NOTIFICATION_MUSIC)
                playerManager.destroy()
                return super.onStartCommand(intent, flags, startId)
            }
            ACTION_CUSTOM_VIEW_OPTIONS_PLAY_OR_PAUSE -> playerManager.play()
            ACTION_CUSTOM_VIEW_OPTIONS_PRE -> playerManager.pre()
            ACTION_CUSTOM_VIEW_OPTIONS_NEXT -> playerManager.next()
            ACTION_CUSTOM_VIEW_OPTIONS_FORWARD -> {
                intent.addCategory("android.intent.category.LAUNCHER")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                startActivity(intent)
            }
        }
        getNotification()
        return START_NOT_STICKY
    }

    private fun getNotificationManager(): NotificationManager {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_LOW)
        manager.createNotificationChannel(channel)
        return manager
    }

    private fun getNotification() {
        val music = PlayerManager.get().getCurrentMusic()
        val builder = Notification.Builder(this, ID)
        //click layout action
        val intent = Intent(this, MainActivity::class.java)
        intent.action = ACTION_CUSTOM_VIEW_OPTIONS_FORWARD
        val click = PendingIntent.getService(this, 0, intent, 0)
        //click the close action
        val closeIntent = Intent(this, PlayerServer::class.java)
        closeIntent.action = ACTION_CUSTOM_VIEW_OPTIONS_CANCEL
        val close: PendingIntent =
            PendingIntent.getService(this, 0, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        //click to play
        val playIntent = Intent(this, PlayerServer::class.java)
        playIntent.action = ACTION_CUSTOM_VIEW_OPTIONS_PLAY_OR_PAUSE
        val play = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        //click to previous music
        val preIntent = Intent(this, PlayerServer::class.java)
        preIntent.action = ACTION_CUSTOM_VIEW_OPTIONS_PRE
        val pre = PendingIntent.getService(this, 0, preIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        //click to next music
        val nextIntent = Intent(this, PlayerServer::class.java)
        nextIntent.action = ACTION_CUSTOM_VIEW_OPTIONS_NEXT
        val next = PendingIntent.getService(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val customView = RemoteViews(this.packageName, R.layout.notification_media_small)
        customView.setImageViewBitmap(R.id.album_cover, MediaUtil.getArtwork(this, music.id,music.albumId, true, false))
        customView.setTextViewText(R.id.name, music.title)
        customView.setTextViewText(R.id.artist, music.artist)
        customView.setImageViewBitmap(R.id.stop_button,
            ImageUtils.drawable2Bitmap(AppCompatResources.getDrawable(this,
            if (PlayerManager.get().isPlaying()) R.drawable.ic_stop else R.drawable.ic_start)))
        customView.setOnClickPendingIntent(R.id.cancel_button, close)
        customView.setOnClickPendingIntent(R.id.stop_button, play)
        customView.setOnClickPendingIntent(R.id.next_button, next)

        val customBigView = RemoteViews(this.packageName, R.layout.notification_media_large)
        customBigView.setImageViewBitmap(R.id.album_cover, MediaUtil.getArtwork(this, music.id,music.albumId, true, false))
        customBigView.setTextViewText(R.id.name, music.title)
        customBigView.setTextViewText(R.id.artist, music.artist)
        customBigView.setImageViewBitmap(R.id.stop_button,
            ImageUtils.drawable2Bitmap(AppCompatResources.getDrawable(this,
                if (PlayerManager.get().isPlaying()) R.drawable.ic_stop else R.drawable.ic_start)))
        customBigView.setOnClickPendingIntent(R.id.cancel_button, close)
        customBigView.setOnClickPendingIntent(R.id.stop_button, play)
        customBigView.setOnClickPendingIntent(R.id.next_button, next)
        customBigView.setOnClickPendingIntent(R.id.pre_button, pre)
        builder
            //设置通知左侧的小图标
            .setSmallIcon(R.drawable.ic_music_on)
            //设置通知不可删除
            .setOngoing(true)
            //设置显示通知时间
            .setShowWhen(true)
            //设置点击通知时的响应事件
            .setContentIntent(click)
            //设置自定义小视图
            .setCustomContentView(customView)
            //设置自定义大视图
            .setCustomBigContentView(customBigView)
        getNotificationManager().notify(MainActivity.NOTIFICATION_MUSIC,builder.build())
    }
}