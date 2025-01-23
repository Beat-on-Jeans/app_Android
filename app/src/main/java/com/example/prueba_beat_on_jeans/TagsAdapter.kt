package com.example.prueba_beat_on_jeans

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TagsAdapter(private val tags: List<Tag>) : RecyclerView.Adapter<TagsAdapter.TagViewHolder>() {

    // ViewHolder class for the RecyclerView
    class TagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTag: TextView = view.findViewById(R.id.TxtTag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        // Inflate the layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        // Bind the data to the views
        val tag = tags[position]
        holder.txtTag.text = tag.tagName
    }

    override fun getItemCount(): Int {
        // Return the total count of items
        return tags.size
    }
}
