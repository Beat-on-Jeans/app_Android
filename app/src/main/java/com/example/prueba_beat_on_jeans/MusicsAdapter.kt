package com.example.prueba_beat_on_jeans

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MusicsAdapter(
    private val context: Context?,
    private val musicList: List<Music>,
    private val onLikeClick: (Music) -> Unit,
    private val onTalkClick: (Music) -> Unit
) : RecyclerView.Adapter<MusicsAdapter.MusicViewHolder>() {

    // ViewHolder que contiene las vistas de un elemento individual del RecyclerView
    inner class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.TxtName)
        val distanceTextView: TextView = view.findViewById(R.id.TxtDistance)
        val descriptionTextView: TextView = view.findViewById(R.id.TxtDescription)
        val imageMusicians: LinearLayout = view.findViewById(R.id.RVImgBackGround)
        val rvTag: RecyclerView = view.findViewById(R.id.RVTags)
        val likeButton: ImageButton = view.findViewById(R.id.BtnHeart)
        val talkButton: ImageButton = view.findViewById(R.id.BtnTalk)
    }

    // Infla el diseño de cada elemento del RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_musicians, parent, false) // Reemplaza "music_item" por el nombre real de tu archivo XML
        return MusicViewHolder(view)
    }

    // Enlaza los datos de un objeto Music a las vistas en un elemento del RecyclerView
    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.nameTextView.text = music.name
        holder.distanceTextView.text = music.distance
        holder.descriptionTextView.text = music.description
        holder.imageMusicians.setBackgroundResource(music.img)
        val adapter = TagsAdapter(music.arrayTags)
        holder.rvTag.adapter = adapter
        holder.rvTag.layoutManager = GridLayoutManager(context,8)
        // Configura las acciones de los botones
        holder.likeButton.setOnClickListener {
            onLikeClick(music)
        }

        holder.talkButton.setOnClickListener {
            onTalkClick(music)
        }
    }

    // Devuelve el número total de elementos en la lista
    override fun getItemCount(): Int {
        return musicList.size
    }
}
