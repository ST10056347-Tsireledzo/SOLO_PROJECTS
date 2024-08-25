package com.example.poe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class WishlistAdapter(context: Context, private val imageNames: Array<String>, private val imageIds: IntArray) :
    ArrayAdapter<String>(context, R.layout.wishlist_grid, imageNames) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val gridView: View

        if (convertView == null) {
            gridView = inflater.inflate(R.layout.wishlist_grid, null)
            val textView = gridView.findViewById<TextView>(R.id.gridItemTextView)
            val imageView = gridView.findViewById<ImageView>(R.id.gridItemImageView)

            textView.text = imageNames[position]
            imageView.setImageResource(imageIds[position])
        } else {
            gridView = convertView
        }

        return gridView
    }
}