package com.example.prueba_beat_on_jeans

import com.google.gson.annotations.SerializedName
import android.os.Parcel
import android.os.Parcelable

data class User(
    @SerializedName("ID") val id: Int,
    @SerializedName("ROL_ID") val rolId: Int,
    @SerializedName("Nombre") val nombre: String,
    @SerializedName("Correo") val correo: String,
    @SerializedName("Contrasena") val contrasena: String,
)

data class UserTemp(
    var rolId: Int = 0,
    var nombre: String = "",
    var correo: String = "",
    var contrasena: String = "",
    var ubicacion: String = "",
    var imagen: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(rolId)
        parcel.writeString(nombre)
        parcel.writeString(correo)
        parcel.writeString(contrasena)
        parcel.writeString(ubicacion)
        parcel.writeString(imagen)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserTemp> {
        override fun createFromParcel(parcel: Parcel): UserTemp {
            return UserTemp(parcel)
        }

        override fun newArray(size: Int): Array<UserTemp?> {
            return arrayOfNulls(size)
        }
    }
}

