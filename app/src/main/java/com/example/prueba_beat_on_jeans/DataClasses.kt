package com.example.prueba_beat_on_jeans

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("ID") val id: Int,
    @SerializedName("Nombre") val nombre: String,
    @SerializedName("Correo") val correo: String,
    @SerializedName("Contrasena") val contrasena: String,
    @SerializedName("ROL_ID") val rolId: Int?,
    @SerializedName("Url_imagen") val imagen: String,
    @SerializedName("Ubicacion") val ubicacion: String
)
