package com.example.prueba_beat_on_jeans

import android.os.Parcel
import android.os.Parcelable

class Chat(
    var locales: String?,
    var musicos: String?,
    var Mensajes: MutableList<Message>,
    var ID: Int,
    var Musico_ID: Int,
    var Local_ID: Int
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
        parcel.writeTypedList(Mensajes)
        parcel.writeInt(ID)
        parcel.writeInt(Musico_ID)
        parcel.writeInt(Local_ID)
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