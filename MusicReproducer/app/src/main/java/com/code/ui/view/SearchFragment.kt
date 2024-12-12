package com.code.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.code.databinding.FragmentSearchBinding
import com.code.ui.controller.SongAdapter
import com.code.ui.controller.Switcher
import com.code.ui.model.CurrentSongProperties


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding  //chiama il suo xml
    private lateinit var parent: Context

    private var songAdapter: SongAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        parent = requireContext()
        readyForOperations()

        binding.recyclerView.setLayoutManager(LinearLayoutManager(this.parent)) //imposta il layout del recycler view
        customSearchView()
        return binding.root
    }


    private fun readyForOperations() {
        songAdapter = SongAdapter(object : SongAdapter.OnItemClickListener {
            override fun onItemClick(song: String?) {
                if (song != null) {
                    /*
                    -controllo
                    -scelta canzone
                    -partenza canzone
                     */
                    CurrentSongProperties.selectSong(song)
                    CurrentSongProperties.startSong()
                    if(HomeFragment.allowsNotifications){   //parte solo se è concessa nell'HomeFragment con il bottone
                        Switcher.startService()
                    }
                }
            }
        })
        binding.recyclerView.setAdapter(songAdapter)

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // Non è necessario gestire il submit
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filtra le canzoni man mano che l'utente digita
                songAdapter!!.filter(newText!!) // Attenzione alla gestione dei null!
                return true
            }
        })
        applyScrollAnimation()
    }
    private fun applyScrollAnimation() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Scorri tutti gli item visibili
                for (i in 0 until recyclerView.childCount) {
                    val child = recyclerView.getChildAt(i)

                    // Se l'elemento non è ancora stato animato, applica l'animazione
                    if (child.translationY == 0f) {
                        // Imposta un'animazione di scivolamento verticale
                        child.translationY = 300f // Offset iniziale (partirà più in basso)
                        child.animate()
                            .translationY(0f) // Ritorna alla posizione originale
                            .setDuration(700) // Durata dell'animazione
                            .setInterpolator(AccelerateDecelerateInterpolator()) // Tipo di interpolatore
                            .start() // Avvia l'animazione
                    }
                }
            }
        })
    }

    @SuppressLint("ResourceAsColor")
    private fun customSearchView() {
        /*
        metodo optional per settare proprietà del SearchVIEW da codice
         */
        binding.searchView.isIconified = false
    }
}




