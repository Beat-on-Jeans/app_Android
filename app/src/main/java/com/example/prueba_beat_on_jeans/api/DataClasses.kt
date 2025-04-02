package com.example.prueba_beat_on_jeans.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("ID") val id: Int,
    @SerializedName("Nombre") val nombre: String,
    @SerializedName("Correo") val correo: String,
    @SerializedName("Contrasena") val contrasena: String,
    @SerializedName("ROL_ID") val rolId: Int?,
    @SerializedName("Url_Imagen") val imagen: String,
    @SerializedName("Ubicacion") val ubicacion: String
)

data class UserRecieved(
    @SerializedName("Contrasena") val contrasena: String?,
    @SerializedName("Correo") val correo: String?,
    @SerializedName("ID") val id: Int,
    @SerializedName("Nombre") val nombre: String,
    @SerializedName("ROL_ID") val rolId: Int?,
    @SerializedName("Ubicacion") val ubicacion: String?,
    @SerializedName("Url_Imagen") val imagen: String,
)

data class UserRecievedWithDescription(
    @SerializedName("Contrasena") val contrasena: String?,
    @SerializedName("Nombre") val nombre: String?,
    @SerializedName("Descripcion") val descripcion: String,
    @SerializedName("ValoracionTotal") val valoracion: Double?,
    @SerializedName("ROL_ID") val rolId: Int?,
    @SerializedName("Ubicacion") val ubicacion: String?,
    @SerializedName("Url_Imagen") val imagen: String,
)

data class UserLogin(
    val correo: String,
    val contrasena: String
)

data class Matches(
              @SerializedName("ID") val id: Int,
              @SerializedName("Nombre") val name: String,
              @SerializedName("Descripcion") val description: String,
              @SerializedName("Generos") val arrayTags: MutableList<String>,
              @SerializedName("Url_Imagen") val img: String): Serializable