package com.example.prueba_beat_on_jeans

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class MusicsAdapter(
    private val context: Context?,
    private val musicList: List<Music>,
    private val onLikeClick: (Music) -> Unit,
    private val onTalkClick: (Music) -> Unit
) : RecyclerView.Adapter<MusicsAdapter.MusicViewHolder>(), CardStackListener {

    private var currentViewHolder: MusicViewHolder? = null // Mantén una referencia al ViewHolder actual

    // ViewHolder que contiene las vistas de un elemento individual del RecyclerView
    inner class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.TxtName)
        val distanceTextView: TextView = view.findViewById(R.id.TxtDistance)
        val descriptionTextView: TextView = view.findViewById(R.id.TxtDescription)
        val imageMusicians: LinearLayout = view.findViewById(R.id.RVImgBackGround)
        val rvTag: RecyclerView = view.findViewById(R.id.RVTags)
        val likeButton: ImageButton = view.findViewById(R.id.BtnHeart)
        val talkButton: ImageButton = view.findViewById(R.id.BtnTalk)
        val leftOverlay: ImageView = view.findViewById(R.id.left_overlay)
        val rightOverlay: ImageView = view.findViewById(R.id.right_overlay)
    }

    // Infla el diseño de cada elemento del RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_musicians, parent, false)
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
        holder.rvTag.layoutManager = GridLayoutManager(context, 8)

        // Configura las acciones de los botones
        holder.likeButton.setOnClickListener {
            onLikeClick(music)
        }

        holder.talkButton.setOnClickListener {
            onTalkClick(music)
        }

        // Asigna el ViewHolder actual
        currentViewHolder = holder
    }

    // Devuelve el número total de elementos en la lista
    override fun getItemCount(): Int {
        return musicList.size
    }

    // Implementación de CardStackListener
    override fun onCardDragging(direction: Direction?, ratio: Float) {
        // Obtén el ViewHolder actual
        currentViewHolder?.let { holder ->
            when (direction) {
                Direction.Left -> {
                    holder.rightOverlay.visibility = View.VISIBLE
                    holder.rightOverlay.alpha = ratio.coerceIn(0.0f, 1.0f)
                }
                Direction.Right -> {
                    holder.leftOverlay.visibility = View.VISIBLE
                    holder.leftOverlay.alpha = ratio.coerceIn(0.0f, 1.0f)
                }
                else -> {
                    holder.leftOverlay.visibility = View.INVISIBLE
                    holder.rightOverlay.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onCardSwiped(direction: Direction?) {
        currentViewHolder?.let { holder ->
            holder.leftOverlay.visibility = View.INVISIBLE
            holder.rightOverlay.visibility = View.INVISIBLE
        }
    }

    override fun onCardRewound() {}
    override fun onCardCanceled() {
        currentViewHolder?.let { holder ->
            holder.leftOverlay.visibility = View.INVISIBLE
            holder.rightOverlay.visibility = View.INVISIBLE
        }
    }

    override fun onCardAppeared(view: View?, position: Int) {}
    override fun onCardDisappeared(view: View?, position: Int) {}
}
