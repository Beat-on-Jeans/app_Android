package com.example.prueba_beat_on_jeans

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("ID") val id: Int,
    @SerializedName("Nombre") val nombre: String,
    @SerializedName("Contrasena") val contrasena: String,
    @SerializedName("Correo") val correo: String,
    @SerializedName("Notificacion_ID") val notificacionId: Int,
    @SerializedName("ROL_ID") val rolId: Int,
    @SerializedName("Notificaciones") val notificaciones: Notificaciones,
    @SerializedName("RolesMovil") val rolesMovil: RolesMovil,
    @SerializedName("Soporte") val soporte: List<Soporte>
)

// Clases auxiliares (si la API las incluye en el JSON):
data class Notificaciones(
    @SerializedName("ID") val id: Int,
    // ... otros campos
)

data class RolesMovil(
    @SerializedName("ID") val id: Int,
    // ... otros campos
)

data class Soporte(
    @SerializedName("ID") val id: Int,
    // ... otros campos
)
