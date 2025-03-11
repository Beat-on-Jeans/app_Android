package com.example.prueba_beat_on_jeans

data class User(
    val id: Int? = null,
    val name: String,
    val password: String,
    val email: String,
    val notificationId: Int? = null,
    val rol: String? = null
)
