package com.example.prueba_beat_on_jeans
import retrofit2.http.GET
import retrofit2.http.*

interface ApiService {

    @GET("Api/Usuarios")
    suspend fun getUsers(): List<User>

    @GET("Api/Usuarios/{id}")
    suspend fun getUser(@Path("id") id: Int): User

    @GET("api/Chats/Local/{userID}")
    suspend fun getLocalChats(@Path("userID") userID: Int): MutableList<Chat>
}

