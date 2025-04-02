package com.example.prueba_beat_on_jeans.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.prueba_beat_on_jeans.R

class ImgAcountAdapter(private val pfp: String,
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
        holder.imgLarge.setBackgroundResource(R.drawable.rounded_img_profile_background);
        holder.imgLarge.setImageResource(imgAcountBackground);
        holder.imgLarge.setClipToOutline(true);

        holder.imgSmall.load(pfp) {
            crossfade(true)
            transformations(RoundedCornersTransformation(12f))
        }

        holder.imgLarge.scaleType = ImageView.ScaleType.CENTER_CROP
        holder.imgSmall.scaleType = ImageView.ScaleType.FIT_XY
    }

    override fun getItemCount(): Int {
        return 1
    }
}