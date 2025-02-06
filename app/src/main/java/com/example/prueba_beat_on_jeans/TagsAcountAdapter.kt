package com.example.prueba_beat_on_jeans

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TagsAcountAdapter(private val tags: List<Tag>) : RecyclerView.Adapter<TagsAcountAdapter.TagViewHolder>() {

    class TagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTag: TextView = view.findViewById(R.id.TxtTagAccount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tag_account, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = tags[position]
        holder.txtTag.text = tag.tagName
    }

    override fun getItemCount(): Int {
        return tags.size
    }
}