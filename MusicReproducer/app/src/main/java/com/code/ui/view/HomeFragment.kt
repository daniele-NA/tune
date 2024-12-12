package com.code.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.code.R
import com.code.databinding.FragmentHomeBinding
import com.code.ui.controller.Switcher
import com.code.ui.model.CurrentSongProperties
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {  //implementa l'interfaccia

    private lateinit var binding: FragmentHomeBinding  //chiama il suo xml
    private lateinit var parent: Context

    private lateinit var handler: Handler   //utilizzato insieme a start per la progress bar
    private lateinit var start: Runnable

    companion object{
         var allowsNotifications:Boolean=true  //variabile accessibile dall'esterno
    }

    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        parent = requireContext()
        handler = Handler(requireActivity().mainLooper) // Usa il Looper dell'Activity
        readyForOperations()
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    private fun readyForOperations() {

        if (!CurrentSongProperties.firstAccess) {
            CurrentSongProperties.initializationParameters(parent)

        }

        if (CurrentSongProperties.mediaPlayer != null && CurrentSongProperties.running) { //se c'è una canzone impostata,parte il check della barra
            loadProgressBar()

            job?.cancel()
            logoAnimation()
        }


        binding.btnStartAndStop.setOnClickListener {
            /*
            va al contrario ovviamente,se sta runnando,si mette il simbolo della pausa e viceversa
             */

            startAnimation(binding.btnStartAndStop)
            when (CurrentSongProperties.running) {
                true -> {//ferma
                    binding.btnStartAndStop.setImageResource(R.drawable.play_icon)
                    CurrentSongProperties.pauseSong()
                }

                false -> {//riparte
                    binding.btnStartAndStop.setImageResource(R.drawable.pause_icon)
                    CurrentSongProperties.startSong()
                    logoAnimation()
                }

            }


        }

        binding.btnSkipPrevious.setOnClickListener {
            //indietro 5 secondi
            startAnimation(binding.btnSkipPrevious)
            CurrentSongProperties.skipPrevious()
        }

        binding.btnSkipNext.setOnClickListener {
            //avanti 5 secondi
            startAnimation(binding.btnSkipNext)
            CurrentSongProperties.skipNext()
        }

        binding.btnNotification.setOnClickListener{ //gestione della notifica
            if(allowsNotifications){
                binding.btnNotification.hint="Notifiche bloccate"
                Switcher.stopService()
                allowsNotifications=!allowsNotifications

            }else{
                binding.btnNotification.hint="Notifiche attivate"
                Switcher.startService()
                allowsNotifications=!allowsNotifications

            }
        }


    }

    private fun loadProgressBar() {
        /*
        si autogestisce,quando il progresso sta a 0,rimuove la callBack
         */
        start = object : Runnable {
            override fun run() {
                // Ottieni il progresso dalla funzione getProgress() di CurrentSongProperties
                if (CurrentSongProperties.mediaPlayer != null) {
                    var progress=0
                    try{
                        progress = CurrentSongProperties.getProgress()
                    }catch (_:Exception){}

                    // Aggiorna la ProgressBar
                    binding.progressBar.progress = progress
                } else {
                    binding.progressBar.progress = 0
                    handler.removeCallbacks(start)
                }


                // Richiama se stessa dopo 1000ms (1 secondo)
                handler.postDelayed(this, 1000)
            }
        }
        if (CurrentSongProperties.mediaPlayer != null) {
            handler.post(start)

        }
    }

    private fun logoAnimation() {
        /*
        codice asincrono
         */
        job = MainScope().launch {
            while (CurrentSongProperties.running) {
                delay(3000)
                startAnimation(binding.imageView)
            }

        }
    }

    private fun startAnimation(component: View) {
        val animation = Runnable {
            component.animate().scaleX(1.2f).scaleY(1.2f)
                .withEndAction {
                    component.scaleX = 1f
                    component.scaleY = 1f
                    component.alpha = 1f
                }
        }
        animation.run()
    }


}





