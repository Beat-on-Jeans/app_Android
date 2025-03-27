package com.example.prueba_beat_on_jeans

import android.os.Parcel
import android.os.Parcelable

class ChatRV(
    val chatID: Int,
    val chatName: String,
    val lastMessage: String,
    val hours: String,
    val notification: Boolean,
    val chatImg: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(chatID)
        parcel.writeString(chatName)
        parcel.writeString(lastMessage)
        parcel.writeString(hours)
        parcel.writeByte(if (notification) 1 else 0)
        parcel.writeInt(chatImg)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatRV> {
        override fun createFromParcel(parcel: Parcel): ChatRV {
            return ChatRV(parcel)
        }

        override fun newArray(size: Int): Array<ChatRV?> {
            return arrayOfNulls(size)
        }
    }
}