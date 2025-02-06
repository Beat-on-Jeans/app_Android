package com.example.prueba_beat_on_jeans

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImgAcountAdapter(private val imgAcount: Int,
                       private val imgAcountBackground: Int) :
    RecyclerView.Adapter<ImgAcountAdapter.ImgAcountViewHolder>() {

    class ImgAcountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgLarge: ImageView = view.findViewById(R.id.imageViewLarge)
        val imgSmall: ImageView = view.findViewById(R.id.imageViewSmall)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgAcountViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image_profile, parent, false)
        return ImgAcountViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImgAcountViewHolder, position: Int) {
        holder.imgLarge.setImageResource(imgAcountBackground)
        holder.imgSmall.setImageResource(imgAcount)
    }

    override fun getItemCount(): Int {
        return 1
    }
}