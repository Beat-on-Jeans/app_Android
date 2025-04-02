package com.example.prueba_beat_on_jeans.classes

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.fragments.ThirdFragment
import com.example.prueba_beat_on_jeans.fragments.FIrstFragment
import com.example.prueba_beat_on_jeans.fragments.FourthFragment
import com.example.prueba_beat_on_jeans.fragments.SecondFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationBar : AppCompatActivity() {
    lateinit var navigation: BottomNavigationView

    private val navMenu = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        for (i in 0 until navigation.menu.size()) {
            val menuItem = navigation.menu.getItem(i)
            menuItem.icon?.mutate()?.setTint(resources.getColor(R.color.black, theme))
        }

        when (item.itemId) {
            R.id.itemFragment1 -> {
                supportFragmentManager.commit {
                    replace<FIrstFragment>(R.id.frame_container)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
            }
            R.id.itemFragment2 -> {
                supportFragmentManager.commit {
                    replace<SecondFragment>(R.id.frame_container)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
            }
            R.id.itemFragment3 -> {
                supportFragmentManager.commit {
                    replace<ThirdFragment>(R.id.frame_container)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
            }
            R.id.itemFragment4 -> {
                supportFragmentManager.commit {
                    replace<FourthFragment>(R.id.frame_container)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
            }
        }

        item.icon?.mutate()?.setTint(resources.getColor(R.color.orange_dark, theme))

        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_bar)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        navigation = findViewById(R.id.navMenu)
        navigation.setOnNavigationItemSelectedListener(navMenu)

        navigation.menu.findItem(R.id.itemFragment1).icon?.mutate()?.setTint(resources.getColor(R.color.orange_dark, theme))

        supportFragmentManager.commit {
            replace<FIrstFragment>(R.id.frame_container)
            setReorderingAllowed(true)
            addToBackStack("replacement")
        }
    }
}
