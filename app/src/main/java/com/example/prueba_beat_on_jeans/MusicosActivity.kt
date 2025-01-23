package com.example.prueba_beat_on_jeans

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MusicosActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musicos)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val RVMusicians = findViewById<RecyclerView>(R.id.RVMusicians)

        var musicsList = mutableListOf(Music("Peggie,23","300ft from you",
                "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"
                , mutableListOf(Tag("Jazz"),Tag("Blues"))
                , R.drawable.human),

            Music("Peggie,23","300ft from you",
                "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"
                , mutableListOf(Tag("Tecno"),Tag("Hardcore"))
                , R.drawable.human),
            )



        val adapter = MusicsAdapter(this,musicsList, { music ->
            // Acción cuando se presiona el botón de "Like"
            println("Like clicked for: ${music.name}")
        }, { music ->
            // Acción cuando se presiona el botón de "Talk"
            println("Talk clicked for: ${music.name}")
        })

        RVMusicians.adapter = adapter
        RVMusicians.layoutManager = LinearLayoutManager(this)

        val btnBack = findViewById<ImageButton>(R.id.ImgBtnBack)

        btnBack.setOnClickListener{
            finish()
        }
    }
}