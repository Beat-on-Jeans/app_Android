package com.example.prueba_beat_on_jeans

import android.os.Parcel
import android.os.Parcelable

class Message(
    var id:Int,
    var chat_ID: Int,
    var emisorID: Int,
    var hora: Int,
    var message: String
    ) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(chat_ID)
        parcel.writeInt(emisorID)
        parcel.writeInt(hora)
        parcel.writeString(message)
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
