package com.example.prueba_beat_on_jeans

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Message(
    @SerializedName("ID") var id: Int?,
    @SerializedName("Chat_ID") var chat_ID: Int,
    @SerializedName("Emisor_ID") var emisor_ID: Int,
    @SerializedName("Hora") var hora: String,
    @SerializedName("Mensaje") var mensaje: String
) : Parcelable {



    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )


    // Segundo constructor (sin ID)
    constructor(Chat_ID: Int, Emisor_ID: Int, Hora: String, Mensaje: String) :
            this(null, Chat_ID, Emisor_ID, Hora, Mensaje) // Llama al constructor principal pasando null como ID


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        id?.let { parcel.writeInt(it) }
        parcel.writeInt(chat_ID)
        parcel.writeInt(emisor_ID)
        parcel.writeString(hora)
        parcel.writeString(mensaje)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }
}