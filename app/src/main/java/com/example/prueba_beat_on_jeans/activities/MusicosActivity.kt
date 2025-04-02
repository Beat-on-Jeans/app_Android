package com.example.prueba_beat_on_jeans.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.prueba_beat_on_jeans.api.Matches
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.adapters.MusicsAdapter
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction


class MusicosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musicos)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val btnBack = findViewById<ImageButton>(R.id.ImgBtnBack)

        btnBack.setOnClickListener{
            finish()
        }
    }



    private fun setCardView() {

        var musicsList = setBetaUsers()

        val cardStackMusicinas = findViewById<CardStackView>(R.id.CVMusicians)

        // Configurar el CardStackLayoutManager
        var manager = CardStackLayoutManager(this, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {}
            override fun onCardSwiped(direction: Direction?) {}
            override fun onCardRewound() {}
            override fun onCardCanceled() {}
            override fun onCardAppeared(view: View?, position: Int) {}
            override fun onCardDisappeared(view: View?, position: Int) {}
        })

        manager.setTranslationInterval(8.0f) // Espaciado entre las tarjetas
        manager.setScaleInterval(0.95f) // Escalado de las tarjetas al fondo
        manager.setSwipeThreshold(0.3f) // Sensibilidad del swipe
        manager.setMaxDegree(20.0f) // Grado máximo de inclinación
        manager.setDirections(Direction.HORIZONTAL) // Swipe horizontal únicamente
        manager.setCanScrollHorizontal(true) // Habilitar scroll horizontal
        manager.setCanScrollVertical(false) // Deshabilitar scroll vertical

        val adapter = MusicsAdapter(this,musicsList, { _ ->  }, { _ ->})

        cardStackMusicinas.layoutManager = manager
        cardStackMusicinas.adapter = adapter
    }

    private fun setBetaUsers(): List<Matches> {
        return listOf()
    }
}