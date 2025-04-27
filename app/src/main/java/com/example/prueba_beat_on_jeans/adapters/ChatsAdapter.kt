package com.example.prueba_beat_on_jeans.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prueba_beat_on_jeans.classes.ChatRV
import com.example.prueba_beat_on_jeans.R

class ChatsAdapter(private val chatList: MutableList<ChatRV>,
                   private val onChatClick: (ChatRV) -> Unit) : RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.bind(chat)
    }

    override fun getItemCount(): Int = chatList.size

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgChat: ImageView = itemView.findViewById(R.id.ImgChat)
        private val txtChatName: TextView = itemView.findViewById(R.id.TxtChatName)
        private val txtLastMessage: TextView = itemView.findViewById(R.id.TxtLastMessage)
        private val txtHours: TextView = itemView.findViewById(R.id.TxtHours)
        private val notificationIcon: ImageView = itemView.findViewById(R.id.Notification)

        fun bind(chat: ChatRV) {
            val hour = chat.hours.split("T")

            imgChat.setImageResource(chat.chatImg)
            txtChatName.text = chat.chatName
            txtLastMessage.hint = chat.lastMessage
            txtHours.hint = hour[0]

            itemView.setOnClickListener{
                onChatClick(chat)
            }
        }
    }
}