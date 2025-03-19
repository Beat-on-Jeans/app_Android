package com.example.prueba_beat_on_jeans
import retrofit2.http.GET
import retrofit2.http.*

interface ApiService {

    @GET("Api/UsuarioMobils")
    suspend fun getUsers(): List<User>

}

