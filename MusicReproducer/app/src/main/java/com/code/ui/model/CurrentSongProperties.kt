package com.code.ui.model

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.code.R
import com.code.ui.controller.Switcher

@SuppressLint(
    "StaticFieldLeak",
    "NewApi"
)  // Si disinteressa delle API per i check di compatibilità
object CurrentSongProperties {

    var firstAccess = false  //serve nell'Home Fragment

    var currentSongName: String = "Nome canzone"   //tiene traccia del nome della canzone che sta correndo
    var currentAuthorName: String = "Artista" //tiene traccia del titolo della canzone che sta correndo

    var mediaPlayer: MediaPlayer? = null
    private var parent: Context? = null  //necessita di un context per la riproduzione

    val songMap = hashMapOf(
        Song("Sant'Allegria", "Ornella Vanoni") to R.raw.santallegria,
        Song("Mockingbird", "Eminem") to R.raw.mockingbird,
        Song("We Are The People", "Empire Of The Sun") to R.raw.wearethepeople,
        Song("BRNBQ", "Sfera Ebbasta") to R.raw.brnbq,
        Song("Calcolatrici", "Sfera Ebbasta,Geolier") to R.raw.calcolatrici,
        Song("Calipso", "Charlie Charles,Sfera Ebbasta") to R.raw.calipso,
        Song("DÁKITI", "Bad Bunny,JHAYCO") to R.raw.dakiti,
        Song("I LOVE IT", "ANNA,Artie 5ive") to R.raw.iloveit,
        Song("Panamera", "NDG") to R.raw.panamera,
        Song("Parole di ghiaccio", "Emis Killa") to R.raw.paroledighiaccio,
        Song("Stamm Fort", "Luchè,Sfera Ebbasta") to R.raw.stammfort,
        Song("TOP G", "Artie 5ive,Sacky") to R.raw.topg,
        Song("Uber", "Sfera Ebbasta") to R.raw.uber,
        Song("Banliue", "Simba la rue,Baby Gang") to R.raw.banliue,
        Song("Black and Yellow", "Wiz Khalifa") to R.raw.blackandyellow,
        Song("Mercedes Nero", "Sfera Ebbasta,Tedua,Izi") to R.raw.mercedesnero,
        Song("Narcotic", "Liquido") to R.raw.narcotic,
        Song("Numb", "Linkin Park") to R.raw.numb,
        Song("PLATA O PLOMO", "Kalionte,Frezza") to R.raw.plataoplomo,
        Song("0 in tasca", "Luis Add,Anthony") to R.raw.zerointasca
    )

    var running: Boolean = false  // Impostato a false di default


    fun initializationParameters(parent: Context?) {
        /*
        inizializza i parametri per renderli pronti all'uso
         */
        this.parent = parent
        mediaPlayer = MediaPlayer()
        this.firstAccess = true
    }

    fun destroyAll() {
        /*
        elimina il mediaPlayer
         */
        mediaPlayer?.apply {
            stop()
            reset()
            release()

        }
        mediaPlayer=null
    }

    fun selectSong(title: String) {
        val songResId: String?
        val currentSong: Song?
        try {
            currentSong = searchSongByTitle(title)  //chiamata al metodo private:Song
            songResId = songMap[currentSong].toString()



            songResId.let { resId ->
                mediaPlayer?.reset()
                mediaPlayer?.setDataSource(
                    parent!!,
                    Uri.parse("android.resource://${parent!!.packageName}/raw/$resId")
                )
                mediaPlayer?.prepare()
                currentSongName = currentSong?.title.toString()
                currentAuthorName = currentSong?.author.toString()


            }
        } catch (_: Exception) {
        }
    }

    fun startSong() {
        //avvia la riproduzione della canzone
        mediaPlayer?.start()
        running = true
    }

    fun pauseSong() {
        //mette in pausa la riproduzione
        mediaPlayer?.pause()
        running = false
    }


    fun getProgress(): Int {
        mediaPlayer?.let { player ->
            val currentPosition = player.currentPosition  // Posizione attuale
            val duration = player.duration  // Durata totale

            // Calcola la percentuale di progresso
            return if (duration > 0) {
                ((currentPosition.toFloat() / duration.toFloat()) * 100).toInt()
            } else {
                0
            }
        }
        return 0
    }

    fun skipNext() {
        /*
        prende la posizione corrente ed aggiunge 5000
         */
        val n: Int? = mediaPlayer?.currentPosition?.plus(5000)
        if (n != null) {
            mediaPlayer?.seekTo(n)
        }
    }

    fun skipPrevious() {
        /*
      prende la posizione corrente e sottrae 5000
       */
        val n: Int? = mediaPlayer?.currentPosition?.minus(5000)
        if (n != null) {
            mediaPlayer?.seekTo(n)
        }
    }

    private fun searchSongByTitle(title: String): Song? {
        /*
        ricerca tramite il titolo la Song che si vuole
         */
        for (song in songMap) {
            if (song.key.title == title) {
                return song.key
            }
        }
        return null
    }
}
