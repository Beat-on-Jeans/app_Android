package com.example.prueba_beat_on_jeans.classes

import com.google.gson.annotations.SerializedName

class EventRV(@SerializedName("ID") val id:Int,
              @SerializedName("Fechas") val date: String,
              @SerializedName("NombreCreador") val creator: String,
              @SerializedName("Imagen") val img: String,
              @SerializedName("NombreFinalizador") val ender: String,
              )