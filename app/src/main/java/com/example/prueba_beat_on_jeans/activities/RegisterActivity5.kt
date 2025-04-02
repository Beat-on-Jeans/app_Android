package com.example.prueba_beat_on_jeans.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prueba_beat_on_jeans.R
import com.example.prueba_beat_on_jeans.api.RetrofitClient
import com.example.prueba_beat_on_jeans.api.User
import com.example.prueba_beat_on_jeans.adapters.GalleryAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class RegisterActivity5 : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST_CODE = 1
    private val imageList = MutableList<Uri?>(1) { null }
    private lateinit var galleryAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register5)

        val recyclerViewImages: RecyclerView = findViewById(R.id.images_grid)
        val buttonContinue: Button = findViewById(R.id.continue_button)
        val imageButtonBack: ImageButton = findViewById(R.id.back_imageButton)
        val nameText: TextView = findViewById(R.id.name_text)

        var itemCount = recyclerViewImages.adapter?.itemCount ?: 0

        galleryAdapter = GalleryAdapter(imageList) { position ->
            openGallery()
            itemCount = 1
        }

        recyclerViewImages.layoutManager = GridLayoutManager(this, 1)
        recyclerViewImages.adapter = galleryAdapter

        buttonContinue.setOnClickListener {
            if(nameText.text.toString() != "" && itemCount != 0){
                MainActivity.UserSession.username = nameText.text.toString()
                uploadImageToServer(imageList[0])
                uploadUser()
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
            }

        }

        imageButtonBack.setOnClickListener {
            val intent = Intent(this, RegisterActivity4::class.java)
            startActivity(intent)
        }
    }

    private fun uploadUser() {
        lifecycleScope.launch {
            delay(6000)
            val user = User(
                0,
                MainActivity.UserSession.username.toString(),
                MainActivity.UserSession.email.toString(),
                MainActivity.UserSession.password.toString(),
                MainActivity.UserSession.rolId,
                MainActivity.UserSession.urlImg.toString(),
                MainActivity.UserSession.location.toString()
            )
            RetrofitClient.instance.registerUser(user).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Usuario registrado con éxito",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Error en el servidor: ${response.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Si la solicitud falló (problema de red, etc.), muestra el error
                    Log.e("Upload user", "Error: ${t.message}")
                    Toast.makeText(
                        applicationContext,
                        "Error de conexión: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        }
        startActivityForResult(
            Intent.createChooser(intent, "Selecciona una imagen"),
            PICK_IMAGE_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val newList = mutableListOf<Uri?>()

            data?.let {
                if (it.data != null) {
                    newList.add(it.data!!)
                }
            }

            while (newList.size < 1) {
                newList.add(null)
            }

            imageList.clear()
            imageList.addAll(newList)
            galleryAdapter.notifyDataSetChanged()
        }
    }

    private fun uploadImageToServer(imageUri: Uri?) {
        if (imageUri == null) {
            Toast.makeText(this, "No hay imagen seleccionada", Toast.LENGTH_SHORT).show()
            return
        }

        val file = getFileFromUri(this, imageUri)
        if (file == null || !file.exists()) {
            Toast.makeText(this, "Error al obtener el archivo", Toast.LENGTH_SHORT).show()
            return
        }

        // Detectar el tipo de archivo automáticamente
        val mimeType = contentResolver.getType(imageUri) ?: "image/*"
        val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        RetrofitClient.instance.uploadImage(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.string()?.let { responseBody ->
                        try {
                            val jsonObject = JSONObject(responseBody)
                            val imageUrl = jsonObject.optString("url", "")

                            if (imageUrl.isNotEmpty()) {
                                MainActivity.UserSession.urlImg = imageUrl
                                Log.e("URL", MainActivity.UserSession.urlImg.toString())
                            } else {
                                Toast.makeText(applicationContext, "No se recibió URL de la imagen", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: JSONException) {
                            Log.e("UPLOAD_ERROR", "Error al parsear JSON: ${e.message}")
                            Toast.makeText(applicationContext, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Log.e("UPLOAD_ERROR", "Error en respuesta: ${response.code()} - $errorBody")
                    Toast.makeText(applicationContext, "Error al subir imagen: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("UPLOAD_ERROR", "Fallo en la conexión: ${t.message}")
                Toast.makeText(applicationContext, "Fallo en la conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val extension = when (context.contentResolver.getType(uri)) {
                "image/jpeg" -> ".jpg"
                "image/png" -> ".png"
                "image/gif" -> ".gif"
                else -> ".jpg"
            }

            val tempFile = File.createTempFile("temp_image", extension, context.cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            Log.e("UPLOAD_ERROR", "Error al obtener archivo: ${e.message}")
            null
        }
    }
}