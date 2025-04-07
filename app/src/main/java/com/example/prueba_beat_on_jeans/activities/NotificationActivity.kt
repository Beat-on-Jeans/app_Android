package com.example.prueba_beat_on_jeans.activities

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.adapters.NotificationsApdater
import com.example.prueba_beat_on_jeans.api.Notification
import com.example.prueba_beat_on_jeans.api.RetrofitClient
import com.example.prueba_beat_on_jeans.misc.FileManager
import kotlinx.coroutines.launch

class NotificationActivity:AppCompatActivity() {

    private var notificationList = mutableListOf<Notification>()
    private var adapter: RecyclerView.Adapter<NotificationsApdater.NotificationViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        getOtherNotifications()
        getLatestNotifications()

        val btnReturn = findViewById<ImageButton>(R.id.BTNReturn)

        btnReturn.setOnClickListener{
            setFileNotifications()
            finish()
        }

    }

    private fun setFileNotifications() {
        FileManager.saveNotifications(this, notificationList)
    }

    private fun getOtherNotifications() {
        if(FileManager.comproveFile(this)){
            notificationList = FileManager.getNotifications(this)
        }else{
            FileManager.saveNotifications(this, listOf())
        }
    }

    private fun getLatestNotifications() {
        lifecycleScope.launch {
            val newNotification = RetrofitClient.instance.getUserLatestNotification(MainActivity.UserSession.id!!)
            if(newNotification?.description.isNullOrEmpty()){
                notificationList.add(newNotification!!)
            }
            setNotifications()
        }

    }

    private fun setNotifications() {
        if(notificationList.size > 0){
            val rvNotification = findViewById<RecyclerView>(R.id.RVNotifications)
            adapter = NotificationsApdater(notificationList)
            rvNotification.adapter = adapter
            rvNotification.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }else{
            adapter = null
        }
    }
}