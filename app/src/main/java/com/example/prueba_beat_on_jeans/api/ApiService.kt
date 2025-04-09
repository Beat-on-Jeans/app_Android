package com.example.prueba_beat_on_jeans.api

import com.example.prueba_beat_on_jeans.classes.Chat
import com.example.prueba_beat_on_jeans.classes.EventRV
import com.example.prueba_beat_on_jeans.classes.Message
import com.example.prueba_beat_on_jeans.classes.Tag
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
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

    @PUT("api/GenerosUsuarios/ActualizarGeneros/{usuarioId}")
    suspend fun actualizarGenerosUsuario(
        @Path("usuarioId") usuarioId: Int,
        @Body generosIds: List<Int>
    ): Response<ResponseBody>

    @GET("api/GenerosUsuarios/ObtenerGeneros/{usuarioId}")
    suspend fun obtenerGenerosUsuario(
        @Path("usuarioId") usuarioId: Int
    ): Response<List<MusicalGender>>

    @PUT("api/UsuarioMobils/{id}/descripcion")
    suspend fun actualizarDescripcionUsuario(
        @Path("id") id: Int,
        @Body descripcion: String
    ): Response<Unit>

    @GET("api/UsuarioMobils/{id}/Descripcion")
    suspend fun getDescripcionUsuario(@Path("id") userId: Int): Response<String>

    @GET("api/Usuarios/Matches_Music/{Ubicacion}/{userID}")
    suspend fun getMusicMatches(@Path("Ubicacion") Ubicacion: String, @Path("userID") userID: Int): MutableList<Matches>

    @POST("api/Matches/{Creador_ID}/{Finalizador_ID}")
    fun createNewMatch(@Path("Creador_ID") creador_id: Int, @Path("Finalizador_ID") finalizador_id: Int): Call<ResponseBody>

    @GET("api/Usuarios/Matches_Locales/{Ubicacion}/{userID}")
    suspend fun getLocalMatches(@Path("Ubicacion") Ubicacion: String, @Path("userID") userID: Int): MutableList<Matches>

    @GET("api/Matches/GetUserMatches/{userId}")
    fun getUserMatches(@Path("userId") userId: Int): Call<List<Match>>

    @PUT("api/Matches/{Creador_ID}/{Finalizador_ID}")
    fun updateMatchStatusToDislike(
        @Path("Creador_ID") creadorId: Int,
        @Path("Finalizador_ID") finalizadorId: Int
    ): Call<ResponseBody>

    @POST("api/Actuacions")
    suspend fun createNewEvent(@Body newEvent: Event): Call<ResponseBody>

    @GET("api/Actuacions/GetUpcomingNewActuacion/{creatorID}/{userID}")
    suspend fun getUpcomingNewActuacion(@Path("creatorID") creatorID: Int,@Path("userID") userID: Int): MutableList<Event>

    @PUT("api/Actuacions/CreateEvent/{event}")
    suspend fun createEvent(@Body event: Event): Event

    @DELETE("api/Actuacions/DeleteEvent/{event}")
    suspend fun deleteEvent(@Body event: Event): Call<ResponseBody>

    @GET("api/Actuacions/GetUserActuaciones/{UserID}")
    suspend fun getUserEvent(@Path("UserID") userId: Int): MutableList<EventRV>

    @GET("api/UsuarioMobils/{UserID}/Valoraciones")
    suspend fun obtainUserRating(@Path("UserID") userId: Int): Float?

    @GET("api/Valoraciones/isNewRatting/{userID}")
    suspend fun obtainIsNewRatting(@Path("userID") userId: Int): MutableList<Rating>?

    @PUT("api/Valoracions/{rating}")
    suspend fun setRatting(@Body rating: Rating): Response<ResponseBody>

    @GET("api/UsuarioMobils/Notificaiones/{userID}")
    suspend fun getUserLatestNotification(@Path("userID") userId: Int): Notification?
}

