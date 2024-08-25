package com.example.poe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView

class Wishlist : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)

        val selectedImageNames = intent.getStringArrayListExtra("selectedImageNames")
        val selectedImageIds = intent.getIntegerArrayListExtra("selectedImageIds")

        if (selectedImageNames != null && selectedImageIds != null) {
            val adapter = WishlistAdapter(this, selectedImageNames.toTypedArray(), selectedImageIds.toIntArray())
            val gridView = findViewById<GridView>(R.id.gridView)
            gridView.adapter = adapter
        }
    }
}