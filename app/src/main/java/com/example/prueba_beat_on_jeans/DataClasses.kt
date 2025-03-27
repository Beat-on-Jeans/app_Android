package com.example.prueba_beat_on_jeans

import com.google.gson.annotations.SerializedName
import android.os.Parcel
import android.os.Parcelable

data class User(
    @SerializedName("Nombre") val nombre: String,
    @SerializedName("Correo") val correo: String,
    @SerializedName("Contrasena") val contrasena: String,
)


