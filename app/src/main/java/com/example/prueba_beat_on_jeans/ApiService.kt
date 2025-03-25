package com.example.prueba_beat_on_jeans
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @GET("Api/Usuarios")
    suspend fun getUsers(): List<User>

    @GET("Api/Usuarios/{id}")
    suspend fun getUser(@Path("id") id: Int): User

    @GET("api/Chats/Local/{userID}")
    suspend fun getLocalChats(@Path("userID") userID: Int): MutableList<Chat>

    @Multipart
    @POST("api/upload")
    fun uploadImage(
        @Part image: MultipartBody.Part,
    ): Call<ResponseBody>

    @POST("api/Usuarios")
    fun createUser(@Body user: User): Call<User>
}

