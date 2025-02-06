package com.example.prueba_beat_on_jeans

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ImgChatsAdapter(private val users: List<ChatRV>) : RecyclerView.Adapter<ImgChatsAdapter.ImgChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_img_chat, parent, false)
        return ImgChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImgChatViewHolder, position: Int) {
        val chat = users[position]
        holder.bind(chat)
    }

    override fun getItemCount(): Int = users.size

    inner class ImgChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgChat: ImageView = itemView.findViewById(R.id.ImgChatMatchs)
        private val txtChat: TextView = itemView.findViewById(R.id.TxtChatMatchs)

        fun bind(chat: ChatRV) {
            imgChat.setImageResource(chat.chatImg)
            txtChat.text = chat.chatName
        }
    }
}
