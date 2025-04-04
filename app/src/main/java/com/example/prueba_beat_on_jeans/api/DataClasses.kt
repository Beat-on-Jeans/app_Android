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
): Serializable

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


class Event(@SerializedName("ID") val id: Int? = null,
            @SerializedName("Fecha") val date: String,
            @SerializedName("Creador_ID") val creador_Id: Int,
            @SerializedName("Finalizador_ID") val finalizador_Id: Int,
            @SerializedName("Estado") var estado: Int): Serializable {
    constructor(date: String, creador_Id: Int, finalizador_Id: Int, estado: Int)
            : this(null,date,creador_Id,finalizador_Id,estado)
}