package com.example.prueba_beat_on_jeans

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MusicsAdapter(
    private val context: Context?,
    private val matchesList: List<Matches>,
    private val onLikeClick: (Matches) -> Unit,
    private val onTalkClick: (Matches) -> Unit
) : RecyclerView.Adapter<MusicsAdapter.MusicViewHolder>(), CardStackListener {

    private var currentViewHolder: MusicViewHolder? = null

    inner class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.TxtName)
        val descriptionTextView: TextView = view.findViewById(R.id.TxtDescription)
        val imageMusicians: ImageView = view.findViewById(R.id.RVImgBackGround)
        val rvTag: RecyclerView = view.findViewById(R.id.RVTags)
        val likeButton: ImageButton = view.findViewById(R.id.BtnHeart)
        val talkButton: ImageButton = view.findViewById(R.id.BtnTalk)
        val leftOverlay: ImageView = view.findViewById(R.id.left_overlay)
        val rightOverlay: ImageView = view.findViewById(R.id.right_overlay)
        val backColor: FrameLayout = view.findViewById(R.id.backColor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_musicians, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = matchesList[position]
        holder.nameTextView.text = music.name
        holder.descriptionTextView.text = music.description
        holder.imageMusicians.load(music.img) {
            crossfade(true)
        }

        val adapter = TagsAdapter(music.arrayTags)
        holder.rvTag.adapter = adapter
        holder.rvTag.layoutManager = GridLayoutManager(context, 8)

        holder.likeButton.setOnClickListener {
            onLikeClick(music)
        }

        holder.talkButton.setOnClickListener {
            onTalkClick(music)
        }

        currentViewHolder = holder
    }

    override fun getItemCount(): Int {
        return matchesList.size
    }

    @SuppressLint("CutPasteId")
    override fun onCardDragging(direction: Direction?, ratio: Float) {
        getTopViewHolder()?.let { holder ->
            when (direction) {
                Direction.Left -> {
                    holder.leftOverlay.visibility = View.INVISIBLE
                    holder.rightOverlay.visibility = View.VISIBLE
                    holder.rightOverlay.alpha = ratio.coerceIn(0.0f, 1.0f)
                    holder.backColor.alpha = ratio.coerceIn(0.0f, 0.6f)
                }
                Direction.Right -> {
                    holder.rightOverlay.visibility = View.INVISIBLE
                    holder.leftOverlay.visibility = View.VISIBLE
                    holder.leftOverlay.alpha = ratio.coerceIn(0.0f, 1.0f)
                    holder.backColor.alpha = ratio.coerceIn(0.0f, 0.6f)
                }
                else -> {
                    holder.leftOverlay.visibility = View.INVISIBLE
                    holder.rightOverlay.visibility = View.INVISIBLE
                    holder.backColor.alpha = 0.0f
                }
            }
        }
    }



    override fun onCardSwiped(direction: Direction?) {
        getTopViewHolder()?.let { holder ->
            holder.leftOverlay.visibility = View.INVISIBLE
            holder.rightOverlay.visibility = View.INVISIBLE
            holder.backColor.alpha = 0.0f
        }
        if (direction == Direction.Right) {
            val layoutManager = (context as? Activity)?.findViewById<CardStackView>(R.id.CVMusicians)?.layoutManager
            if (layoutManager is CardStackLayoutManager) {
                val topPosition = layoutManager.topPosition - 1 // Se resta 1 porque la carta ya se movió
                if (topPosition in matchesList.indices) {
                    val userLiked = matchesList[topPosition]
                    onLikeClick(userLiked) // Llamamos a la acción de "Like"
                }
            }
        }
    }

    override fun onCardRewound() {}
    override fun onCardCanceled() {
        getTopViewHolder()?.let { holder ->
            holder.leftOverlay.visibility = View.INVISIBLE
            holder.rightOverlay.visibility = View.INVISIBLE
            holder.backColor.alpha = 0.0f
        }
    }

    override fun onCardAppeared(view: View?, position: Int) {}
    override fun onCardDisappeared(view: View?, position: Int) {}

    private fun getTopViewHolder(): MusicViewHolder? {
        val layoutManager = (context as? Activity)?.findViewById<CardStackView>(R.id.CVMusicians)?.layoutManager
        if (layoutManager is CardStackLayoutManager) {
            val topPosition = layoutManager.topPosition
            return (context as? Activity)?.findViewById<CardStackView>(R.id.CVMusicians)
                ?.findViewHolderForAdapterPosition(topPosition) as? MusicViewHolder
        }
        return null
    }
}
