package com.example.poe

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.poe.R

class SuggestionAdapter(context: Context, private val imageNames: Array<String>, private val imageIds: IntArray) : ArrayAdapter<String>(context, R.layout.grid_item, imageNames) {

    private val selectedImageNames = mutableListOf<String>()
    private val selectedImageIds = mutableListOf<Int>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val gridView: View

        if (convertView == null) {
            gridView = inflater.inflate(R.layout.grid_item, null)
            val textView = gridView.findViewById<TextView>(R.id.gridItemTextView)
            val imageView = gridView.findViewById<ImageView>(R.id.gridItemImageView)

            textView.text = imageNames[position]
            imageView.setImageResource(imageIds[position])

            gridView.setOnClickListener {
                selectedImageNames.add(imageNames[position])
                selectedImageIds.add(imageIds[position])

                val intent = Intent(context, Wishlist::class.java).apply {
                    putStringArrayListExtra("selectedImageNames", ArrayList(selectedImageNames))
                    putIntegerArrayListExtra("selectedImageIds", ArrayList(selectedImageIds))
                }
                context.startActivity(intent)
            }
        } else {
            gridView = convertView
        }

        return gridView
    }
}