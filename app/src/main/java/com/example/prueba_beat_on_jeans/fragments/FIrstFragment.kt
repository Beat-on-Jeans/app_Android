package com.example.prueba_beat_on_jeans

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting
import kotlinx.coroutines.launch
import coil.load
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    private var matchesList = mutableListOf<Matches>()

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
        val cardStackMusicinas = view.findViewById<CardStackView>(R.id.CVMusicians)

        val adapter = MusicsAdapter(requireContext(), matchesList, { userLiked ->
                lifecycleScope.launch {
                    if (MainActivity.UserSession.rolId == 1){
                       val call =  RetrofitClient.instance.createNewMusicMatch(userLiked.id,MainActivity.UserSession.id!!)
                        call.enqueue(object : Callback<ResponseBody> {
                               override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                   if (response.isSuccessful) {
                                       Log.d("API_RESPONSE", "Match creado correctamente")
                                   } else {
                                       Log.e("API_ERROR", "Error en la respuesta: ${response.errorBody()?.string()}")
                                   }
                               }

                               override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                   Log.e("API_ERROR", "Fallo en la petición: ${t.message}")
                               }
                           })
                    }
                    else {
                        val call = RetrofitClient.instance.createNewLocalMatch(MainActivity.UserSession.id!!,userLiked.id)
                        call.enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                if (response.isSuccessful) {
                                    Log.d("API_RESPONSE", "Match creado correctamente")
                                } else {
                                    Log.e("API_ERROR", "Error en la respuesta: ${response.errorBody()?.string()}")
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Log.e("API_ERROR", "Fallo en la petición: ${t.message}")
                            }
                        })
                    }
                }
            }

        ) {
        }

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

        when(MainActivity.UserSession.rolId){
            1 -> setLocalMatches(view)
            2 -> setMusiciansMatches(view)
        }

        var pfp: ImageView = view.findViewById(R.id.profile_picture)
        val notification_button: ImageView = view.findViewById(R.id.notification)

        val imageUrl = MainActivity.UserSession.urlImg

        pfp.load(imageUrl) {
            crossfade(true)
        }

        return view
    }

    private fun setMusiciansMatches(view: View) {
        lifecycleScope.launch {
            try {
                matchesList = RetrofitClient.instance.getMusicMatches(MainActivity.UserSession.location!!,MainActivity.UserSession.id!!)
                setCardView(view)
            }catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}", e)
                Toast.makeText(
                    context,
                    "Error al conectar con el servidor",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setLocalMatches(view: View) {
        lifecycleScope.launch {
            try {
                matchesList = RetrofitClient.instance.getLocalMatches(MainActivity.UserSession.location!!,MainActivity.UserSession.id!!)
                setCardView(view)
            }catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}", e)
                Toast.makeText(
                    context,
                    "Error al conectar con el servidor",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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
}