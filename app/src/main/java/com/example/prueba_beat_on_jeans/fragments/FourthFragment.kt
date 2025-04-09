package com.example.prueba_beat_on_jeans.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.activities.SettingsActivity
import com.example.prueba_beat_on_jeans.classes.Tag
import com.example.prueba_beat_on_jeans.adapters.TagsAcountAdapter
import com.example.prueba_beat_on_jeans.activities.MainActivity
import com.example.prueba_beat_on_jeans.adapters.EventAdapter
import com.example.prueba_beat_on_jeans.adapters.ImgAcountAdapter
import com.example.prueba_beat_on_jeans.api.Event
import com.example.prueba_beat_on_jeans.api.MusicalGender
import com.example.prueba_beat_on_jeans.api.Rating
import com.example.prueba_beat_on_jeans.api.RetrofitClient
import com.example.prueba_beat_on_jeans.classes.ChangeGendersDialog
import com.example.prueba_beat_on_jeans.classes.EditDescriptionDialog
import com.example.prueba_beat_on_jeans.classes.EventRV
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

class FourthFragment : Fragment(), ChangeGendersDialog.OnGendersSelectedListener,
    EditDescriptionDialog.OnDescriptionChangedListener {

    private val generosMusicales = listOf(
        "Rock", "Pop", "Jazz", "Hip Hop", "Clásica", "Reggaeton", "Blues", "Folk", "Salsa", "Bachata",
        "Reggae", "Country", "Indie", "Punk", "Disco", "Heavy Metal", "Gospel", "R&B", "Electronica",
        "Techno", "K-pop", "Trap", "Ska", "Tango", "Flamenco", "Soul", "Psychedelic", "Metalcore",
        "Grunge", "Glam Rock"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fourth, container, false)
        val rvAcTags = view.findViewById<RecyclerView>(R.id.RVTagsAccount)
        chargeDescription()
        getEvents(view)
        setAccount(view, rvAcTags)
        return view
    }

    @SuppressLint("DefaultLocale", "ClickableViewAccessibility")
    private fun setAccount(view: View, rvAcTags: RecyclerView) {
        val rcAcImgBac = view.findViewById<RecyclerView>(R.id.RVImgsAccount)
        val btnGenders = view.findViewById<Button>(R.id.button_genders)
        val txtAcName = view.findViewById<TextView>(R.id.TxtAccountName)
        val txtAcDesc = view.findViewById<TextView>(R.id.TxtAccountDescription)
        val txtRating = view.findViewById<TextView>(R.id.TxtRating)
        val rbRating = view.findViewById<RatingBar>(R.id.RBRating)

        // Configuración de la imagen de perfil
        val adapterImgs = ImgAcountAdapter(
            MainActivity.UserSession.urlImg.toString(),
            R.drawable.background
        )
        rcAcImgBac.adapter = adapterImgs
        rcAcImgBac.layoutManager = LinearLayoutManager(context)

        // Configuración del nombre de usuario
        txtAcName.text = MainActivity.UserSession.username

        // Configuración del listener para editar descripción
        txtAcDesc.setOnClickListener {
            showEditDescriptionDialog(txtAcDesc.text.toString())
        }

        // Botón de configuración
        val btnSetting = view.findViewById<ImageButton>(R.id.BtnSettings)
        btnSetting.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Cargar los géneros del usuario
        lifecycleScope.launch {
            val tags = obtainUserGenders()
            val adapterTags = TagsAcountAdapter(tags)
            rvAcTags.adapter = adapterTags
            rvAcTags.layoutManager = GridLayoutManager(context, 4)
        }

        // Botón para cambiar géneros
        btnGenders.setOnClickListener {
            lifecycleScope.launch {
                val selectedIds = obtainSelectedGenders()
                ChangeGendersDialog(
                    generosMusicales,
                    selectedIds,
                    this@FourthFragment
                ).show(parentFragmentManager, "ChangeGendersDialog")
            }
        }

        lifecycleScope.launch {
            val rating = obtainUserRating()
            if(rating != null){
                txtRating.text = String.format("%.2f",rating)
                rbRating.rating = rating
            }
            rbRating.setOnTouchListener { _, _ -> true }
        }

/*
        lifecycleScope.launch {
            val isNewRatting = obtainIsEndEvent()
            if(isNewRatting != null && isNewRatting.size != 0){
                val dialog = NewRattingEventDialog.newInstance(isNewRatting[0])
                dialog.show(parentFragmentManager, "NewRattingEventDialog")
            }
        }

 */
    }


    private fun getEvents(view: View){
        lifecycleScope.launch {
            val eventList = RetrofitClient.instance.getUserEvent(MainActivity.UserSession.id!!)
            delay(1000)
            setEvents(view,eventList)

        }
    }
    private fun setEvents(view: View, eventList: MutableList<EventRV>){
        val rvEvent = view.findViewById<RecyclerView>(R.id.RVEvents)
        val adaperEvents = EventAdapter(eventList)
        rvEvent.minimumHeight = (eventList.size * 300)
        rvEvent.adapter = adaperEvents
        rvEvent.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)

    }

    private fun showEditDescriptionDialog(currentDescription: String) {
        val dialog = EditDescriptionDialog.newInstance(currentDescription, this)
        dialog.show(parentFragmentManager, "EditDescriptionDialog")
    }

    override fun onDescriptionChanged(newDescription: String) {
        lifecycleScope.launch {
            try {
                val userId = MainActivity.UserSession.id ?: return@launch
                val response = RetrofitClient.instance.actualizarDescripcionUsuario(userId, newDescription)

                if (response.isSuccessful) {
                    showToast("Descripción actualizada")
                    view?.findViewById<TextView>(R.id.TxtAccountDescription)?.text = newDescription
                } else {
                    Log.e("API", "Error al actualizar descripción: ${response.code()}")
                    showToast("Error al actualizar descripción")
                }
            } catch (e: Exception) {
                Log.e("API", "Excepción al actualizar descripción", e)
                showToast("Error de conexión")
            }

        }
    }

    private suspend fun obtainUserGenders(): List<Tag> {
        val usuarioId = MainActivity.UserSession.id
        return try {
            val response: Response<List<MusicalGender>> =
                RetrofitClient.instance.obtenerGenerosUsuario(usuarioId!!)

            if (response.isSuccessful) {
                response.body()?.mapNotNull { apiGender ->
                    val localIndex = generosMusicales.indexOfFirst {
                        it.equals(apiGender.genero, ignoreCase = true)
                    }
                    if (localIndex != -1) Tag(localIndex, generosMusicales[localIndex]) else null
                } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun obtainSelectedGenders(): List<Int> {
        return obtainUserGenders().map { it.id }
    }

    override fun onGendersSelected(selectedIndices: List<Int>) {
        lifecycleScope.launch {
            try {
                val generosIdsParaAPI = selectedIndices.map { it + 1 }
                val response = RetrofitClient.instance.actualizarGenerosUsuario(
                    MainActivity.UserSession.id!!,
                    generosIdsParaAPI
                )
                if (response.isSuccessful) {
                    showToast("Géneros actualizados")
                    val tags = obtainUserGenders()
                    view?.findViewById<RecyclerView>(R.id.RVTagsAccount)?.apply {
                        adapter = TagsAcountAdapter(tags)
                        layoutManager = GridLayoutManager(context, 4)
                    }
                } else {
                    showToast("Error al actualizar")
                }
            } catch (e: Exception) {
                showToast("Error de conexión")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun chargeDescription() {
        lifecycleScope.launch {
            try {
                val userId = MainActivity.UserSession.id ?: return@launch
                val response = RetrofitClient.instance.getDescripcionUsuario(userId)

                if (response.isSuccessful) {
                    val descripcion = response.body() ?: ""
                    view?.findViewById<TextView>(R.id.TxtAccountDescription)?.text = descripcion
                } else {
                    Log.e("API", "Error al obtener descripción: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Excepción al obtener descripción", e)
            }
        }
    }

    private suspend fun obtainUserRating(): Float? {
        return RetrofitClient.instance.obtainUserRating(MainActivity.UserSession.id!!)
    }



    private suspend fun obtainIsEndEvent() {
        //return RetrofitClient.instance.obtainIsNewRatting(MainActivity.UserSession.id!!)
    }
}