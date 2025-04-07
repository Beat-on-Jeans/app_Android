package com.example.prueba_beat_on_jeans.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.classes.EventRV

class EventAdapter(private val eventList: MutableList<EventRV>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = eventList.size

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgEvent: ImageView = itemView.findViewById(R.id.ImgEventBg)
        private val txtTitleEvent: TextView = itemView.findViewById(R.id.TxtEventTitle)
        private val txtDate: TextView = itemView.findViewById(R.id.TxtDate)

        @SuppressLint("SetTextI18n")
        fun bind(event: EventRV) {
            val date = event.date.split('T')

            imgEvent.load(event.img)
            txtTitleEvent.text = "${event.creator} + ${event.ender}"
            txtDate.text = date[0]
        }
    }
}