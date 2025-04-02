package com.example.prueba_beat_on_jeans.api
import com.example.prueba_beat_on_jeans.classes.Chat
import com.example.prueba_beat_on_jeans.classes.Message
import com.example.prueba_beat_on_jeans.classes.Tag
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @GET("Api/Usuarios")
    suspend fun getUsers(): List<User>

    @GET("Api/Usuarios/{userID}")
    suspend fun getUser(@Path("userID") userID: Int): User

    @GET("Api/LocalChatsUser/{userID}")
    suspend fun getLocalChats(@Path("userID") userID: Int): MutableList<Chat>

    @GET("Api/MusicChatsUser/{userID}")
    suspend fun getMusicChats(@Path("userID") userID: Int): MutableList<Chat>

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

    @GET("api/Usuarios/Musicos")
    fun getMusicos(): Call<List<UserRecievedWithDescription>>

    @GET("api/Usuarios/Locales")
    fun getLocales(): Call<List<UserRecievedWithDescription>>

    @POST("api/Usuarios")
    fun createUser(@Body user: User): Call<User>

    @GET("Api/MusicGenders/{userID}")
    suspend fun getMusicGenders(@Path("userID") userID: Int): MutableList<Tag>

    @GET("api/Usuarios/Matches_Music/{Ubicacion}/{userID}")
    suspend fun getMusicMatches(@Path("Ubicacion") Ubicacion: String, @Path("userID") userID: Int): MutableList<Matches>

    @POST("api/Matches/{Local_ID}/{Musico_ID}")
    fun createNewMatch(@Path("Local_ID") local_id: Int, @Path("Musico_ID") musico_id: Int): Call<ResponseBody>

    @GET("api/Usuarios/Matches_Locales/{Ubicacion}/{userID}")
    suspend fun getLocalMatches(@Path("Ubicacion") Ubicacion: String, @Path("userID") userID: Int): MutableList<Matches>
}

