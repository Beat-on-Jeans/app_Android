package com.example.prueba_beat_on_jeans.classes

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.fragments.ThirdFragment
import com.example.prueba_beat_on_jeans.fragments.FIrstFragment
import com.example.prueba_beat_on_jeans.fragments.FourthFragment
import com.example.prueba_beat_on_jeans.fragments.SecondFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationBar : AppCompatActivity() {
    lateinit var navigation: BottomNavigationView

    private val navMenu = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        // Desmarcar el icono de todos los elementos del menú
        for (i in 0 until navigation.menu.size()) {
            val menuItem = navigation.menu.getItem(i)
            menuItem.icon?.mutate()?.setTint(resources.getColor(R.color.black, theme))
        }

        when (item.itemId) {
            R.id.itemFragment1 -> {
                supportFragmentManager.commit {
                    // Agregar FirstFragment en lugar de reemplazarlo
                    add(R.id.frame_container, FIrstFragment())
                    setReorderingAllowed(true)
                    addToBackStack("FirstFragment") // Esto asegura que puedes volver a este fragmento
                }
            }
            R.id.itemFragment2 -> {
                supportFragmentManager.commit {
                    // Agregar SecondFragment
                    add(R.id.frame_container, SecondFragment())
                    setReorderingAllowed(true)
                    addToBackStack("SecondFragment")
                }
            }
            R.id.itemFragment3 -> {
                supportFragmentManager.commit {
                    // Agregar ThirdFragment
                    add(R.id.frame_container, ThirdFragment())
                    setReorderingAllowed(true)
                    addToBackStack("ThirdFragment")
                }
            }
            R.id.itemFragment4 -> {
                supportFragmentManager.commit {
                    // Agregar FourthFragment
                    add(R.id.frame_container, FourthFragment())
                    setReorderingAllowed(true)
                    addToBackStack("FourthFragment")
                }
            }
        }

        // Cambiar el color del icono del item seleccionado
        item.icon?.mutate()?.setTint(resources.getColor(R.color.orange_dark, theme))

        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_bar)

        // Configuración de la vista a pantalla completa
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        navigation = findViewById(R.id.navMenu)
        navigation.setOnNavigationItemSelectedListener(navMenu)

        // Inicializar el primer fragmento cuando se carga la actividad
        navigation.menu.findItem(R.id.itemFragment1).icon?.mutate()?.setTint(resources.getColor(R.color.orange_dark, theme))

        // Agregar el primer fragmento al inicio
        supportFragmentManager.commit {
            add(R.id.frame_container, FIrstFragment()) // Usamos add en lugar de replace
            setReorderingAllowed(true)
            addToBackStack("FirstFragment") // Aseguramos que puedes regresar a este fragmento
        }
    }
}
