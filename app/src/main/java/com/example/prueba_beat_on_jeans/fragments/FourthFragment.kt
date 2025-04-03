package com.example.prueba_beat_on_jeans.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
import com.example.prueba_beat_on_jeans.adapters.ImgAcountAdapter
import com.example.prueba_beat_on_jeans.api.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Response

class FourthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fourth, container, false)
        val rvAcTags = view.findViewById<RecyclerView>(R.id.RVTagsAccount)
        setAccount(view, rvAcTags)
        return view
    }

    private fun setAccount(view: View, rvAcTags: RecyclerView) {
        val rcAcImgBac = view.findViewById<RecyclerView>(R.id.RVImgsAccount)
        val txtAcName = view.findViewById<TextView>(R.id.TxtAccountName)
        val txtAcDesc = view.findViewById<TextView>(R.id.TxtAccountDescription)

        rvAcTags.setOnClickListener{
            Log.e("Hoal", "aaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        }

        val adapterImgs = ImgAcountAdapter(
            MainActivity.UserSession.urlImg.toString(),
            R.drawable.background
        )
        rcAcImgBac.adapter = adapterImgs
        rcAcImgBac.layoutManager = LinearLayoutManager(context)

        txtAcName.text = MainActivity.UserSession.username

        // Aquí, obtendremos los géneros y configuraremos el RecyclerView
        lifecycleScope.launch {
            val tags = obtenerGenerosDelUsuario()
            val adapterTags = TagsAcountAdapter(tags)
            rvAcTags.adapter = adapterTags
            rvAcTags.layoutManager = GridLayoutManager(context, 4)
        }

        txtAcDesc.text = "Capturing killer fashion shots by day, rocking out at concerts by night. Up for grabbing coffee and seeing if we vibe?"

        val btnSetting = view.findViewById<ImageButton>(R.id.BtnSettings)
        btnSetting.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun obtenerGenerosSeleccionados(): List<Int> {
        return listOf(1, 3, 5)
    }

    // Obtener géneros musicales del usuario desde la API
    private suspend fun obtenerGenerosDelUsuario(): List<Tag> {
        val usuarioId = MainActivity.UserSession.id  // Obtén el ID del usuario (ajústalo según tu lógica)
        val response: Response<List<com.example.prueba_beat_on_jeans.api.MusicalGender>> =
            RetrofitClient.instance.obtenerGenerosUsuario(usuarioId!!)

        return if (response.isSuccessful) {
            val generos = response.body()
            if (generos != null) {
                generos.map { genero ->
                    Tag(genero.id, genero.genero)
                }
            } else {
                showToast("No se encontraron géneros musicales.")
                emptyList()
            }
        } else {
            showToast("Error: ${response.code()} - ${response.message()}")
            emptyList()
        }
    }

    // Función para mostrar los mensajes de error o éxito
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
