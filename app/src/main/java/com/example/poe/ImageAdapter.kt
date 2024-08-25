package com.example.poe

// ImageAdapter.kt
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ImageAdapter(private val images: List<ImageData>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.image_card, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = images[position]

        holder.textViewName.text = currentItem.name
        holder.textViewDescription.text = currentItem.description
        holder.ratingBar.rating = currentItem.rating

        // Load image with Picasso
        Picasso.get().load(currentItem.url).into(holder.imageView)
    }

    override fun getItemCount() = images.size
}