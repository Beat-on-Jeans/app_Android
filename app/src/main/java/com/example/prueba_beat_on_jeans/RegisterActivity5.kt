package com.example.prueba_beat_on_jeans

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.UUID

class RegisterActivity5 : AppCompatActivity() {
    private lateinit var userTemp: UserTemp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register5)

        // Llama a la función para subir la imagen
        val imageUri = Uri.parse("ruta/a/tu/imagen.jpg") // Ejemplo de URI
        uploadImage(imageUri)
    }

    private fun uploadImage(imageUri: Uri) {
        val file = File(getRealPathFromURI(imageUri))

        // Obtener el tipo MIME de la imagen
        val mimeType = contentResolver.getType(imageUri) ?: "image/*"  // Default a "image/*" si no se puede obtener el MIME

        // Obtener la extensión del archivo
        val extension = getFileExtension(mimeType)

        // Generar un nombre único para el archivo
        val uniqueFileName = UUID.randomUUID().toString() + ".$extension"

        // Convertir la URI a un archivo y crear la solicitud para subir
        val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())  // Crear el requestBody con el tipo MIME
        val body = MultipartBody.Part.createFormData("file", uniqueFileName, requestBody)

        val apiService = RetrofitClient.instance  // Usamos la instancia correcta
        val call = apiService.uploadImage(body)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity5, "Imagen subida exitosamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity5, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@RegisterActivity5, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getFileExtension(mimeType: String?): String {
        return when (mimeType) {
            "image/jpeg", "image/jpg" -> "jpg"
            "image/png" -> "png"
            "image/gif" -> "gif"
            "image/bmp" -> "bmp"
            else -> "jpg"  // Si no es una imagen compatible, se usa jpg como predeterminado
        }
    }

    private fun getRealPathFromURI(uri: Uri): String {
        // Esta función debe retornar la ruta real del archivo a partir de la URI
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val idx = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val filePath = cursor?.getString(idx ?: 0)
        cursor?.close()
        return filePath ?: ""
    }
}
