package com.example.prueba_beat_on_jeans

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

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

        // Encontrar el CardStackView en el diseño inflado
        val cardStackMusicinas = view.findViewById<CardStackView>(R.id.CVMusicians)

        // Configurar el CardStackLayoutManager
        val manager = CardStackLayoutManager(requireContext(), object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {}
            override fun onCardSwiped(direction: Direction?) {}
            override fun onCardRewound() {}
            override fun onCardCanceled() {}
            override fun onCardAppeared(view: View?, position: Int) {}
            override fun onCardDisappeared(view: View?, position: Int) {}
        })

        manager.setTranslationInterval(8.0f) // Espaciado entre las tarjetas
        manager.setScaleInterval(0.95f) // Escalado de las tarjetas al fondo
        manager.setSwipeThreshold(0.3f) // Sensibilidad del swipe
        manager.setMaxDegree(20.0f) // Grado máximo de inclinación
        manager.setDirections(Direction.HORIZONTAL) // Swipe horizontal únicamente
        manager.setCanScrollHorizontal(true) // Habilitar scroll horizontal
        manager.setCanScrollVertical(false) // Deshabilitar scroll vertical

        val adapter = MusicsAdapter(requireContext(), musicsList, { _ -> }, { _ -> })

        // Asignar el manager y el adapter al CardStackView
        cardStackMusicinas.layoutManager = manager
        cardStackMusicinas.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        // Configurar el CardStackView después de inflar el diseño
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