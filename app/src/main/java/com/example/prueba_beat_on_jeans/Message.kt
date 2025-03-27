package com.example.prueba_beat_on_jeans

import android.os.Parcel
import android.os.Parcelable

class Message(
    var ID: Int?,
    var Chat_ID: Int,
    var Emisor_ID: Int,
    var Hora: String,
    var Mensaje: String
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
        ID?.let { parcel.writeInt(it) }
        parcel.writeInt(Chat_ID)
        parcel.writeInt(Emisor_ID)
        parcel.writeString(Hora)
        parcel.writeString(Mensaje)
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