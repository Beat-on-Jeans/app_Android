package com.example.prueba_beat_on_jeans

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationBar : AppCompatActivity() {
    lateinit var navigation : BottomNavigationView

    private val navMenu = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId){
            R.id.itemFragment1 -> {
                supportFragmentManager.commit {
                    replace<FIrstFragment>(R.id.frame_container)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }
        }

        when (item.itemId){
            R.id.itemFragment2 -> {
                supportFragmentManager.commit {
                    replace<SecondFragment>(R.id.frame_container)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }
        }

        when (item.itemId){
            R.id.itemFragment3 -> {
                supportFragmentManager.commit {
                    replace<ThirdFragment>(R.id.frame_container)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }
        }

        when (item.itemId){
            R.id.itemFragment4 -> {
                supportFragmentManager.commit {
                    replace<FourthFragment>(R.id.frame_container)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_bar)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        navigation = findViewById(R.id.navMenu)
        navigation.setOnNavigationItemSelectedListener(navMenu)

        supportFragmentManager.commit {
            replace<FIrstFragment>(R.id.frame_container)
            setReorderingAllowed(true)
            addToBackStack("replacement")
        }

    }
}