package com.example.prueba_beat_on_jeans

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FIrstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FIrstFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun setCardView(view: View) {
        val musicsList = setBetaUsers()

        val cardStackMusicinas = view.findViewById<CardStackView>(R.id.CVMusicians)

        val adapter = MusicsAdapter(requireContext(), musicsList, { _ -> }, { _ -> })

        val manager = CardStackLayoutManager(requireContext(), object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                adapter.onCardDragging(direction, ratio)
            }

            override fun onCardSwiped(direction: Direction?) {
                adapter.onCardSwiped(direction)
            }

            override fun onCardRewound() {}
            override fun onCardCanceled() {
                adapter.onCardCanceled()
            }
            override fun onCardAppeared(view: View?, position: Int) {}
            override fun onCardDisappeared(view: View?, position: Int) {}
        })

        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(false)

        cardStackMusicinas.layoutManager = manager
        cardStackMusicinas.adapter = adapter

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        setCardView(view)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FIrstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setBetaUsers(): List<Music> {
        return mutableListOf(Music("Peggie,23","300ft from you",
            "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"
            , mutableListOf(Tag("Jazz"),Tag("Blues"))
            , R.drawable.human),
            Music("Peggie,23","300ft from you",
                "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"
                , mutableListOf(Tag("Jazz"),Tag("Blues"))
                , R.drawable.human),
            Music("Peggie,23","300ft from you",
                "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"
                , mutableListOf(Tag("Jazz"),Tag("Blues"))
                , R.drawable.human),
            Music("Peggie,23","300ft from you",
                "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"
                , mutableListOf(Tag("Jazz"),Tag("Blues"))
                , R.drawable.human),
            Music("Peggie,23","300ft from you",
                "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"
                , mutableListOf(Tag("Jazz"),Tag("Blues"))
                , R.drawable.human),
            Music("Peggie,23","300ft from you",
                "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"
                , mutableListOf(Tag("Jazz"),Tag("Blues"))
                , R.drawable.human),
            Music("Peggie,23","300ft from you",
                "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"
                , mutableListOf(Tag("Jazz"),Tag("Blues"))
                , R.drawable.human),
            Music("Peggie,23","300ft from you",
                "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"
                , mutableListOf(Tag("Jazz"),Tag("Blues"))
                , R.drawable.human),
            Music("Peggie,23","300ft from you",
                "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"
                , mutableListOf(Tag("Jazz"),Tag("Blues"))
                , R.drawable.human),Music("Peggie,23","300ft from you",
                "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"
                , mutableListOf(Tag("Jazz"),Tag("Blues"))
                , R.drawable.human)
        )
    }
}