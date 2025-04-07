package com.example.prueba_beat_on_jeans.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import android.widget.ImageView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting
import kotlinx.coroutines.launch
import coil.load
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.activities.MainActivity
import com.example.prueba_beat_on_jeans.adapters.MusicsAdapter
import com.example.prueba_beat_on_jeans.api.Matches
import com.example.prueba_beat_on_jeans.api.RetrofitClient
import com.example.prueba_beat_on_jeans.classes.NavigationBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var adapter: MusicsAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [FIrstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FIrstFragment : Fragment() {

    private var matchesList = mutableListOf<Matches>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        when(MainActivity.UserSession.rolId) {
            1 -> setMusiciansMatches(view)
            2 -> setLocalMatches(view)
        }
        setCardView(view)

        val pfp: ImageButton = view.findViewById(R.id.profile_picture)
        val imageUrl = MainActivity.UserSession.urlImg

        pfp.load(imageUrl) {
            crossfade(true)
        }

        pfp.setOnClickListener {
            activity?.findViewById<BottomNavigationView>(R.id.navMenu)?.selectedItemId = R.id.itemFragment4
        }

        return view
    }

    private fun setCardView(view: View) {
        val cardStackView = view.findViewById<CardStackView>(R.id.CVMusicians)

        adapter = MusicsAdapter(
            requireContext(),
            matchesList,
            { userLiked -> handleLike(userLiked) },
            { userLiked -> handleDislike(userLiked) }
        ).apply {
            setCardStackView(cardStackView)
        }

        val manager = CardStackLayoutManager(requireContext(), adapter).apply {
            setStackFrom(StackFrom.None)
            setVisibleCount(4)
            setTranslationInterval(8.0f)
            setScaleInterval(0.95f)
            setSwipeThreshold(0.3f)
            setMaxDegree(20.0f)
            setDirections(Direction.HORIZONTAL)
            setCanScrollHorizontal(true)
            setCanScrollVertical(false)
        }

        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
    }


    private fun handleLike(userLiked: Matches) {
        lifecycleScope.launch {
            try {
                val call = RetrofitClient.instance.createNewMatch(
                    MainActivity.UserSession.id!!,
                    userLiked.id
                )
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Log.d("API_RESPONSE", "Match creado correctamente")
                            matchesList.removeFirstOrNull()
                            adapter.notifyDataSetChanged() // Notifica al adaptador sobre el cambio en la lista
                        } else {
                            Log.e("API_ERROR", "Error en la respuesta: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("API_ERROR", "Fallo en la petición: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error en la creación del match: ${e.message}")
            }
        }
    }

    private fun handleDislike(userLiked: Matches) {
        lifecycleScope.launch {
            try {
                val call = RetrofitClient.instance.updateMatchStatusToDislike(
                    MainActivity.UserSession.id!!,
                    userLiked.id
                )
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Log.d("API_RESPONSE", "Match actualizado a estado 1 (rechazado)")
                            matchesList.removeFirstOrNull()
                            adapter.notifyDataSetChanged() // Notifica al adaptador sobre el cambio en la lista
                        } else {
                            Log.e("API_ERROR", "Error en la respuesta: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("API_ERROR", "Fallo en la petición: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error en la actualización del estado: ${e.message}")
            }
        }
    }


    private fun setMusiciansMatches(view: View) {
        lifecycleScope.launch {
            try {
                matchesList = RetrofitClient.instance.getMusicMatches(
                    MainActivity.UserSession.location!!,
                    MainActivity.UserSession.id!!
                )
                setCardView(view)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}", e)
                Toast.makeText(context, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setLocalMatches(view: View) {
        lifecycleScope.launch {
            try {
                matchesList = RetrofitClient.instance.getLocalMatches(
                    MainActivity.UserSession.location!!,
                    MainActivity.UserSession.id!!
                )
                setCardView(view)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}", e)
                Toast.makeText(context, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }
}