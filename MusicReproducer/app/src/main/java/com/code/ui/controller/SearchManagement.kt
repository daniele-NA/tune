package com.code.ui.controller


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.code.R
import com.code.ui.model.CurrentSongProperties
import java.util.Locale

/*

queste classi comunica direttamente con il database

 */


class SongAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    private val filteredSongs: MutableList<String> = ArrayList()

    private val inheritedSongs: MutableList<String> = ArrayList()

    init {
        /*
        si estrae le stringhe dalle canzoni
         */
        getDataFromDatabase()

    }

    // Interfaccia per gestire i click sugli item,si fa l'override
    interface OnItemClickListener {
        fun onItemClick(song: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        /*
        crea la view e setta il layout DEGLI ELEMENTI per il recycler view
         */
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = filteredSongs[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int {
        return filteredSongs.size
    }

    // Metodo per filtrare la lista
    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
        filteredSongs.clear()
        for (song in inheritedSongs) {
            if (song.lowercase(Locale.getDefault())
                    .startsWith(query.lowercase(Locale.getDefault()))
            ) {
                filteredSongs.add(song)
            }
        }
        this.notifyDataSetChanged()
    }

    private fun getDataFromDatabase() {
        /*
      aggiunge per ogni canzone dell'hashmap,la propria chiave (titolo)
       */
        CurrentSongProperties.songMap.forEach {
            inheritedSongs.add(it.key.title)
        }
        filteredSongs.addAll(inheritedSongs)
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val songTitle: TextView = itemView.findViewById(R.id.viewItem)

        fun bind(song: String?) {
            songTitle.text = song
            itemView.setOnClickListener {
                startAnimation(itemView)
                listener.onItemClick(song) // Gestione del click sull'item
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
}
