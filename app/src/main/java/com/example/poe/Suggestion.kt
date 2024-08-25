package com.example.poe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView



class Suggestion : AppCompatActivity() {

    private val imageNames = arrayOf("Singapore", "Kuwait", "Saudi Arabia", "Manila", "Qatar", "Bangkok", "Romania", "Bahrain")
    private val imageIds = intArrayOf(
        R.drawable.singapore, R.drawable.kuwait, R.drawable.saudi_arabia, R.drawable.manila,
        R.drawable.qatar, R.drawable.bangkok, R.drawable.romania, R.drawable.bahrain
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestion)

        val gridView = findViewById<GridView>(R.id.gridView)
        val adapter = SuggestionAdapter(this, imageNames, imageIds)
        gridView.adapter = adapter

        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, Wishlist::class.java)
            intent.putStringArrayListExtra("selectedImageNames", arrayListOf(imageNames[position]))
            intent.putIntegerArrayListExtra("selectedImageIds", arrayListOf(imageIds[position]))
            startActivity(intent)
        }
    }
}