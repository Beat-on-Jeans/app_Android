package com.example.prueba_beat_on_jeans
import retrofit2.http.GET
import retrofit2.http.*

interface ApiService {

    @GET("api/UsuarioMobils")
    suspend fun getUsers(): List<User>

    @GET("api/UsuariosCSharps/{id}")
    suspend fun getUser(@Path("id") id: Int): User

    @PUT("api/UsuariosCSharps/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): User

    @POST("api/UsuariosCSharps")
    suspend fun createUser(@Body user: User): User

    @DELETE("api/UsuariosCSharps/{id}")
    suspend fun deleteUser(@Path("id") id: Int)
}

