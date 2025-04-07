package com.example.prueba_beat_on_jeans.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.prueba_beat_on_jeans.api.Matches
import com.example.prueba_beat_on_jeans.R
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting

class MusicsAdapter(
    private val context: Context?,
    private var matchesList: MutableList<Matches>,
    private val onLikeClick: (Matches) -> Unit,
    private val onDislikeClick: (Matches) -> Unit
) : RecyclerView.Adapter<MusicsAdapter.MusicViewHolder>(), CardStackListener {

    private var currentViewHolder: MusicViewHolder? = null
    private var cardStackView: CardStackView? = null

    fun setCardStackView(stackView: CardStackView) {
        this.cardStackView = stackView
    }

    inner class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.TxtName)
        val descriptionTextView: TextView = view.findViewById(R.id.TxtDescription)
        val imageMusicians: ImageView = view.findViewById(R.id.RVImgBackGround)
        val rvTag: RecyclerView = view.findViewById(R.id.RVTags)
        val likeButton: ImageButton = view.findViewById(R.id.like)
        val dislikeButton: ImageButton = view.findViewById(R.id.dislike)
        val leftOverlay: ImageView = view.findViewById(R.id.left_overlay)
        val rightOverlay: ImageView = view.findViewById(R.id.right_overlay)
        val backColor: FrameLayout = view.findViewById(R.id.backColor)

        init {
            likeButton.setOnClickListener {
                performSwipe(Direction.Right)
            }

            dislikeButton.setOnClickListener {
                performSwipe(Direction.Left)
            }
        }

        private fun performSwipe(direction: Direction) {
            cardStackView?.let { stackView ->
                val manager = stackView.layoutManager as? CardStackLayoutManager
                manager?.let {
                    val setting = SwipeAnimationSetting.Builder()
                        .setDirection(direction)
                        .setDuration(300)
                        .setInterpolator(AccelerateInterpolator())
                        .build()
                    it.setSwipeAnimationSetting(setting)
                    stackView.swipe()
                }
            }
        }
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
        holder.rvTag.layoutManager = GridLayoutManager(context, 5)

        currentViewHolder = holder
    }

    override fun getItemCount(): Int = matchesList.size

    fun removeTopCard() {
        if (matchesList.isNotEmpty()) {
            matchesList.removeAt(0)
            notifyItemRemoved(0)
        }
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        currentViewHolder?.let { holder ->
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
        currentViewHolder?.let { holder ->
            holder.leftOverlay.visibility = View.INVISIBLE
            holder.rightOverlay.visibility = View.INVISIBLE
            holder.backColor.alpha = 0.0f
        }

        direction?.let {
            if (matchesList.isNotEmpty()) {
                val currentCard = matchesList[0]
                when (direction) {
                    Direction.Right -> {
                        onLikeClick(currentCard)
                    }
                    Direction.Left -> {
                        onDislikeClick(currentCard)
                    }
                    else -> {}
                }
                notifyItemRemoved(0)
            }
        }
    }

    override fun onCardRewound() {}
    override fun onCardCanceled() {
        currentViewHolder?.let { holder ->
            holder.leftOverlay.visibility = View.INVISIBLE
            holder.rightOverlay.visibility = View.INVISIBLE
            holder.backColor.alpha = 0.0f
        }
    }

    override fun onCardAppeared(view: View?, position: Int) {}
    override fun onCardDisappeared(view: View?, position: Int) {}
}
