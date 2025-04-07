package com.example.prueba_beat_on_jeans.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prueba_beat_on_jeans.R

class TagsAdapter(private val tags: MutableList<String>) : RecyclerView.Adapter<TagsAdapter.TagViewHolder>() {

    class TagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTag: TextView = view.findViewById(R.id.TxtTag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = tags[position]
        holder.txtTag.text = tag
    }

    override fun getItemCount(): Int = tags.size
}
