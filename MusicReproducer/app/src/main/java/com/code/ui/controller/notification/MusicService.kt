package com.code.ui.controller.notification

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.code.R
import com.code.ui.model.CurrentSongProperties
import com.code.ui.view.MainActivity

class MusicService : Service() {
    /*
    gestione nomi azioni e canali
     */
    private val PREV = "play"
    private val PLAY_PAUSE = "play_pause"
    private val NEXT = "next"


    private val channelId = "music_notification_channel"
    private val channelName="CK PLAYER"
    private val notificationId = 1

    /*
    3 bottoni + l'intent che riporta all'activity
     */
    private var playPauseIntent: PendingIntent? = null
    private var previousIntent: PendingIntent? = null
    private var nextIntent: PendingIntent? = null
    private var openAppIntent: PendingIntent? = null

    private var notification: Notification = Notification()


    @SuppressLint("ForegroundServiceType", "NewApi")
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        buildPendingIntent()

        sendNotification()

    }


    private fun sendNotification() {

        val mediaSession = MediaSessionCompat(this, "music")



        notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(CurrentSongProperties.currentSongName)
            .setContentText(CurrentSongProperties.currentAuthorName)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(openAppIntent)  // Aggiungi il PendingIntent qui
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .addAction(R.drawable.skip_previous_notification_item, "prev", previousIntent)
            .addAction(
                getPlayPauseIcon(),
                "play",
                playPauseIntent
            )
            .addAction(R.drawable.skip_next_notification_icon, "next", nextIntent)
            .build()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startForeground(notificationId, notification)
            }
        } else {
            startForeground(notificationId, notification)
        }
    }

    /*
    gestione bottoni
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (intent.action) {
                PREV -> {
                    CurrentSongProperties.skipPrevious()
                }

                PLAY_PAUSE -> {
                    if (CurrentSongProperties.running) {
                        CurrentSongProperties.pauseSong()
                    } else {
                        CurrentSongProperties.startSong()
                    }
                    sendNotification()
                }

                NEXT -> {
                    CurrentSongProperties.skipNext()
                }
            }
        }
        return START_STICKY
    }

    private fun getPlayPauseIcon() =
        if (CurrentSongProperties.running) R.drawable.pause_notification_icon else R.drawable.play_notification_icon

    private fun buildPendingIntent() {
        // PendingIntent che riapre la MainActivity quando clicchi sulla notifica
        this.openAppIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        var intent = Intent(this, MusicService::class.java).apply {
            action = PREV
        }
        previousIntent =
            PendingIntent.getService(
                this,
                1,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )


        intent = Intent(this, MusicService::class.java).apply {
            action = PLAY_PAUSE
        }
        playPauseIntent =
            PendingIntent.getService(
                this,
                2,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )


        intent = Intent(this, MusicService::class.java).apply {
            action = NEXT
        }
        nextIntent =
            PendingIntent.getService(
                this,
                3,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
    }


    // onBind() restituisce il nostro Binder
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }


}



