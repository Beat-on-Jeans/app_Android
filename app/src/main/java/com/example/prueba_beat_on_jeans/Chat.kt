package com.example.prueba_beat_on_jeans

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Chat(
    @SerializedName("UsuarioMobil") var locales: String?,
    @SerializedName("UsuarioMobil1") var musicos: String?,
    @SerializedName("Mensajes") var mensajes: MutableList<Message>,
    @SerializedName("ID") var id: Int,
    @SerializedName("UsuarioMobil_Musico_ID") var musico_ID: Int,
    @SerializedName("UsuarioMobil_Local_ID") var local_ID: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        mutableListOf<Message>().apply {
            parcel.readTypedList(this, Message.CREATOR)
        },
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(locales)
        parcel.writeString(musicos)
        parcel.writeTypedList(mensajes)
        parcel.writeInt(id)
        parcel.writeInt(musico_ID)
        parcel.writeInt(local_ID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Chat> {
        override fun createFromParcel(parcel: Parcel): Chat {
            return Chat(parcel)
        }

        override fun newArray(size: Int): Array<Chat?> {
            return arrayOfNulls(size)
        }
    }
}