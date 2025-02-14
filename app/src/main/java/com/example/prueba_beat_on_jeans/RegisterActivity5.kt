package com.example.prueba_beat_on_jeans

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge

class RegisterActivity5 : AppCompatActivity() {

    private val PICK_IMAGES_REQUEST_CODE = 1

    private val imageList = MutableList<Uri?>(12) { null }

    private lateinit var galleryAdapter: GalleryAdapter

    private val photoList = ArrayList<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register5)

        val recyclerViewImages: RecyclerView = findViewById(R.id.images_grid)
        val buttonContinue: Button = findViewById(R.id.continue_button)
        val imageButtonBack: ImageButton = findViewById(R.id.back_imageButton)

        galleryAdapter = GalleryAdapter(imageList) { position ->
            Toast.makeText(this, "Item $position clickeado", Toast.LENGTH_SHORT).show()
            openGallery()
        }

        recyclerViewImages.layoutManager = GridLayoutManager(this, 3) // 3 columnas en el grid
        recyclerViewImages.adapter = galleryAdapter

        buttonContinue.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        imageButtonBack.setOnClickListener {
            val intent = Intent(this, RegisterActivity4::class.java)
            startActivity(intent)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(
            Intent.createChooser(intent, "Selecciona imágenes"),
            PICK_IMAGES_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val newList = mutableListOf<Uri?>()

            data?.let {
                if (it.clipData != null) {
                    val count = it.clipData!!.itemCount
                    for (i in 0 until count) {
                        if (newList.size < 12) {
                            val imageUri = it.clipData!!.getItemAt(i).uri
                            newList.add(imageUri)
                        }
                    }
                } else if (it.data != null) {
                    newList.add(it.data!!)
                }
            }

            while (newList.size < 12) {
                newList.add(null)
            }

            imageList.clear()
            imageList.addAll(newList)
            galleryAdapter.notifyDataSetChanged()
        }
    }
}
