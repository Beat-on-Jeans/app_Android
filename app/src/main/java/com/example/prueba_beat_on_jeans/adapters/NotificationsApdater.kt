package com.example.prueba_beat_on_jeans.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.api.Notification

class NotificationsApdater(private val context: Context,
                           private val notificationList: MutableList<Notification>,
                           private val onItemClick: (Notification) -> Unit): RecyclerView.Adapter<NotificationsApdater.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.bind(notification)

        holder.itemView.setOnClickListener {
            onItemClick(notification)
        }
    }

    override fun getItemCount(): Int = notificationList.size

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtNotification: TextView = itemView.findViewById(R.id.txtNotification)

        fun bind(notification: Notification) {
            when(notification.id){
                1-> txtNotification.text = context.getString(R.string.chat_notification)
                2-> txtNotification.text = context.getString(R.string.chat_notification)
                3-> txtNotification.text = context.getString(R.string.chat_notification)
                4-> txtNotification.text = context.getString(R.string.chat_notification)
                5-> txtNotification.text = context.getString(R.string.chat_notification)
            }
        }
    }
}