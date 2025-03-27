package com.example.prueba_beat_on_jeans

import android.os.Parcel
import android.os.Parcelable

class Chat(
    var messagesList: MutableList<Message>,
    var id: Int,
    var musico_ID: Int,
    var local_ID: Int
    ) : Parcelable {

    constructor(parcel: Parcel) : this(
        mutableListOf<Message>().apply {
            parcel.readTypedList(this, Message.CREATOR)
        },
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(messagesList)
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