package com.example.testexercisealexm.presentation.poi_details

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.testexercisealexm.R
import com.example.testexercisealexm.presentation.GlideApp

class WikiImagesAdapter constructor(private val images: List<String>) :
    RecyclerView.Adapter<WikiImagesAdapter.ImageViewHolder>() {

    class ImageViewHolder constructor(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val iv = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false) as ImageView
        return ImageViewHolder(iv)
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        GlideApp.with(holder.imageView.context)
            .load(images[position])
            .centerCrop()
            .placeholder(R.drawable.ic_image_placeholder)
            .into(holder.imageView)
    }
}