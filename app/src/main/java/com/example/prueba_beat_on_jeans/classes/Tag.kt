package com.example.prueba_beat_on_jeans.classes

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Tag(@SerializedName("ID") val id: Int,
          @SerializedName("Nombre_Genero") val tagName: String):Serializable