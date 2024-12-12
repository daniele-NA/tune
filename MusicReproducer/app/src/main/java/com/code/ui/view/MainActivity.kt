package com.code.ui.view

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.code.R
import com.code.databinding.ActivityMainBinding
import com.code.ui.controller.Switcher
import com.code.ui.model.CurrentSongProperties


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeFragment: HomeFragment = HomeFragment()
    private val searchFragment: SearchFragment = SearchFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makeCurrentFragment(homeFragment)
        /*
        da colore alla barra in basso dei 3 pulsanti android
         */
        window.navigationBarColor = ContextCompat.getColor(this, R.color.purple_900)

        readyForOperations()


    }

    private fun makeCurrentFragment(fragment: Fragment) {
        /*
        gestisce i frammenti
         */
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }

    private fun readyForOperations() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_bar_menu -> makeCurrentFragment(homeFragment)
                R.id.search_bar_menu -> makeCurrentFragment(searchFragment)
            }
            true
        }
    }

    override fun onResume() {   //si specifica allo Switcher l'attività corrente
        super.onResume()
        Switcher.newCurrentActivity(this)
    }

    override fun onDestroy() {
        /*
        Se la notifica è permessa, continua a riprodursi in background,altrimenti si ferma il servizo
         */
        if(!HomeFragment.allowsNotifications){
            CurrentSongProperties.destroyAll()

        }
        super.onDestroy()
    }




}

