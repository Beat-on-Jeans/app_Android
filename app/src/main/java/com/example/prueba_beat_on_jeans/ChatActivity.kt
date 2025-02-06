package com.example.prueba_beat_on_jeans

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChatActivity : AppCompatActivity()
{

    private lateinit var chat: ChatRV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setView()
        val imgChater = findViewById<ImageButton>(R.id.BtnBack)

        imgChater.setOnClickListener{
            finish()
        }
    }

    private fun setView() {
        chat = obtainIntent()

        val txtNameChat = findViewById<TextView>(R.id.TxtNameChat)
        val imgChater = findViewById<ImageView>(R.id.ImgChater)

        txtNameChat.text = chat.chatName
        imgChater.setImageResource(chat.chatImg)

    }

    private fun obtainIntent(): ChatRV {
        val intent = intent
        val chat = intent.getParcelableExtra<ChatRV>(ChatInfo.CHATINFO) as ChatRV
        return chat
    }

    object ChatInfo{
        const val CHATINFO = "CHATINFO"
    }
}