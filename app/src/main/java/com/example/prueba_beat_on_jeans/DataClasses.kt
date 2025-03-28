package com.example.prueba_beat_on_jeans

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

data class User(
    @SerializedName("ID") val id: Int,
    @SerializedName("Nombre") val nombre: String,
    @SerializedName("Correo") val correo: String,
    @SerializedName("Contrasena") val contrasena: String,
    @SerializedName("ROL_ID") val rolId: Int?,
    @SerializedName("Url_imagen") val imagen: String,
    @SerializedName("Ubicacion") val ubicacion: String
)

data class Address(
    private val codi_via: String,
    private val codi_carrer_ine: String,
    private val tipus_via: String,
    val shortName: String,
    private val officialName: String,
    val minNumber: Int?,
    val maxNumber: Int?
)

