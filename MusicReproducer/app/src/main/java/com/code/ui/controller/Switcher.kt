package com.code.ui.controller

import android.annotation.SuppressLint
import android.app.Activity

import android.content.Intent
import androidx.activity.ComponentActivity
import com.code.ui.controller.notification.MusicService
import com.code.ui.model.CurrentSongProperties


@SuppressLint("StaticFieldLeak","NewApi")
object Switcher {

    /*
    inserisci qui tutte le attività
     */
    private var serviceIntent: Intent = Intent()


    private val NOTIFICATION_SERVICE: Class<MusicService> = MusicService::class.java

     private var currentActivity: ComponentActivity? = null

    fun newCurrentActivity(a: Activity?) {
        /*
        prende sempre l'attività corrente,attraverso il metodo onResume di ogni attività
         */
        currentActivity = a as ComponentActivity?
        serviceIntent = Intent(currentActivity, NOTIFICATION_SERVICE)
    }



    //partenza servizio
    fun startService() {
        currentActivity?.startService(serviceIntent)
    }

    fun stopService() {
        currentActivity?.stopService(serviceIntent)
    }


}






