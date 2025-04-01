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

    @GET("Api/Chat/{chatID}")
    suspend fun getChat(@Path("chatID") chatID: Int): Chat

    @GET("Api/ChatsUser/{userID}")
    suspend fun getChats(@Path("userID") userID: Int): MutableList<Chat>

    @POST("Api/Mensajes")
    suspend fun insertNewMessage(@Body message: Message)

    @Multipart
    @POST("api/upload")
    fun uploadImage(
        @Part image: MultipartBody.Part,
    ): Call<ResponseBody>

    @POST("api/User")
    fun registerUser(@Body user: User): Call<ResponseBody>

    @POST("api/User/Login")
    fun loginUser(@Body user: User): Call<User>

    @POST("api/Usuarios")
    fun createUser(@Body user: User): Call<User>

    @GET("Api/MusicGenders/{userID}")
    suspend fun getMusicGenders(@Path("userID") userID: Int): MutableList<Tag>

    @GET("api/Usuarios/Matches_Music/{Ubicacion}/{userID}")
    suspend fun getMusicMatches(@Path("Ubicacion") Ubicacion: String, @Path("userID") userID: Int): MutableList<Matches>

    @GET("api/Usuarios/Matches_Locales/{Ubicacion}/{userID}")
    suspend fun getLocalMatches(@Path("Ubicacion") Ubicacion: String, @Path("userID") userID: Int): MutableList<Matches>

    @POST("api/Matches_Music/{Local_ID}/{Musico_ID}")
    fun createNewMusicMatch(@Path("Local_ID") local_id: Int, @Path("Musico_ID") musico_id: Int): Call<ResponseBody>

    @POST("api/Matches_Local/{Local_ID}/{Musico_ID}")
    fun createNewLocalMatch(@Path("Local_ID") local_id: Int, @Path("Musico_ID") musico_id: Int): Call<ResponseBody>
}

