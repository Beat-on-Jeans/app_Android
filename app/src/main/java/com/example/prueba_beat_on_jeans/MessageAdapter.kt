package com.example.prueba_beat_on_jeans

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(val context: Context,
                     val messageList: List<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1){
            // infalte recieve
            val view: View = LayoutInflater.from(context).inflate(R.layout.item_sent, parent, false)
            return SentViewHolder(view)
        }else{
            // infalte recieve
            val view: View = LayoutInflater.from(context).inflate(R.layout.item_recieved, parent, false)
            return ReceiveViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.Mensaje
        }else{
            val viewHolder = holder as ReceiveViewHolder
            holder.receieveMessage.text = currentMessage.Mensaje
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        return if(currentMessage.Emisor_ID == MainActivity.UserSession.id){
            ITEM_SENT
        }else{
            ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int = messageList.size


    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val receieveMessage: TextView = itemView.findViewById(R.id.txtRecievedMessage)
    }

    class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val sentMessage: TextView = itemView.findViewById(R.id.txtSentMessage)
    }

}