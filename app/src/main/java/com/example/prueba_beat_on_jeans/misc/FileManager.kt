package com.example.prueba_beat_on_jeans.misc

import android.content.Context
import com.example.prueba_beat_on_jeans.api.Notification
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class FileManager {

    companion object {
        fun comproveFile(context: Context): Boolean {
            val jsonFilePath = context.filesDir.toString() + "/Notifications.json"
            val jsonFile = File(jsonFilePath)
            return jsonFile.exists() && jsonFile.length() != 0L
        }

        fun getNotifications(context: Context): MutableList<Notification> {
            val jsonFilePath = context.filesDir.toString() + "/Notifications.json"
            val jsonFile = FileReader(jsonFilePath)
            val listUsersType = object : TypeToken<MutableList<Notification>>() {}.type
            val arrayUsersData: MutableList<Notification> = Gson().fromJson(jsonFile, listUsersType)
            return arrayUsersData
        }

        fun saveNotifications(context: Context, notificationList: List<Notification>) {
            val jsonFilePath = context.filesDir.toString() + "/Notifications.json"
            val jsonFile = FileWriter(jsonFilePath)
            val gson = Gson()
            val jsonElement = gson.toJson(notificationList)
            jsonFile.write(jsonElement)
            jsonFile.close()
        }
    }
}