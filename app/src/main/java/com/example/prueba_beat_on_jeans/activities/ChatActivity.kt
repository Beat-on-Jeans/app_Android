package com.example.prueba_beat_on_jeans.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prueba_beat_on_jeans.classes.Chat
import com.example.prueba_beat_on_jeans.classes.ChatRV
import com.example.prueba_beat_on_jeans.classes.Message
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.api.RetrofitClient
import com.example.prueba_beat_on_jeans.api.User
import com.example.prueba_beat_on_jeans.adapters.MessageAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatActivity : AppCompatActivity() {

    private lateinit var chat: ChatRV
    private var job: Job? = null
    private var chatStats = Chat(null,null, mutableListOf(),0,0,0)
    private var userChat = User(
        0, null.toString(), null.toString(), null.toString(), 0,
        imagen = TODO(),
        ubicacion = TODO()
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        obtainStats()
        val imgChater = findViewById<ImageButton>(R.id.BtnBack)

        imgChater.setOnClickListener {
            finish()
        }
        iniciarLlamadas()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun iniciarLlamadas() {
        job = lifecycleScope.launch {
            while (isActive) { // Repite mientras esté activo
                obtainStats()
                delay(10_000) // Espera 10 segundos
            }
        }
    }

    fun detenerLlamadas() {
        job?.cancel() // Cancela la repetición
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtainStats(){
        chat = obtainIntent()
        obtainChat()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    private fun setView() {

        val currentDateTime = LocalDateTime.now()
        // Formatear la fecha en el formato deseado
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS")
        val formattedDate = currentDateTime.format(formatter)

        val txtNameChat = findViewById<TextView>(R.id.TxtNameChat)
        val imgChater = findViewById<ImageView>(R.id.ImgChater)

        val rvMessages = findViewById<RecyclerView>(R.id.RVMessages)
        val btnSend = findViewById<ImageButton>(R.id.BtnSendMsg)
        val edTxtMessage = findViewById<EditText>(R.id.editTextMessage)

        txtNameChat.text = userChat.nombre
        imgChater.setImageResource(chat.chatImg)

        val adapter = MessageAdapter(this, chatStats.mensajes)
        adapter.notifyDataSetChanged()
        rvMessages.adapter = adapter
        rvMessages.layoutManager = LinearLayoutManager(this)

        btnSend.setOnClickListener {
            if ((edTxtMessage.text.isNotEmpty() and edTxtMessage.text.isNotBlank()) or (edTxtMessage.text.length > 500)) {

                val message = Message(chatStats.id,
                    MainActivity.UserSession.id!!,formattedDate ,edTxtMessage.text.toString())
                chatStats.mensajes.add(message)
                adapter.notifyDataSetChanged()
                lifecycleScope.launch {
                    RetrofitClient.instance.insertNewMessage(message)
                }
                edTxtMessage.text.clear()
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtainChat() {
        lifecycleScope.launch {
            val currentChat = RetrofitClient.instance.getChat(chat.chatID)
            when (MainActivity.UserSession.rolId){
                1 -> userChat = RetrofitClient.instance.getUser(currentChat.musico_ID)
                2 -> userChat = RetrofitClient.instance.getUser(currentChat.local_ID)
            }
            chatStats = currentChat
            setView()
        }
    }

    private fun obtainIntent(): ChatRV {
        val intent = intent
        val chat = intent.getParcelableExtra<ChatRV>(ChatInfo.CHATINFO) as ChatRV
        return chat
    }

    object ChatInfo {
        const val CHATINFO = "CHATINFO"
    }
}