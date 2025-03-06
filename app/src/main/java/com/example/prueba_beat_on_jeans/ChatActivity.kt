package com.example.prueba_beat_on_jeans

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity()
{
    private var messagesList = mutableListOf<Message>()
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

    @SuppressLint("NotifyDataSetChanged")
    private fun setView() {
        chat = obtainIntent()

        val txtNameChat = findViewById<TextView>(R.id.TxtNameChat)
        val imgChater = findViewById<ImageView>(R.id.ImgChater)
        val rvMessages = findViewById<RecyclerView>(R.id.RVMessages)
        val btnSend = findViewById<ImageButton>(R.id.BtnSendMsg)
        val edTxtMessage = findViewById<EditText>(R.id.editTextMessage)


        txtNameChat.text = chat.chatName
        imgChater.setImageResource(chat.chatImg)

        val adapter = MessageAdapter(this,
                                    getMessages())
        rvMessages.adapter = adapter
        rvMessages.layoutManager = LinearLayoutManager(this)

        btnSend.setOnClickListener{
            if((edTxtMessage.text.isNotEmpty() and edTxtMessage.text.isNotBlank()) or (edTxtMessage.text.length > 500)){
                var message = Message(edTxtMessage.text.toString(),2)
                messagesList.add(message)
                adapter.notifyDataSetChanged()
                edTxtMessage.text.clear()
            }
        }
    }

    private fun getMessages(): MutableList<Message> {
        messagesList.addAll(mutableListOf(Message("Hola",1),
                                                Message("Que tal?",1),
                                                Message("¡Hola! Bien y tu?",2),
                                                Message("Bien también",1)))
        return messagesList
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