package com.example.poe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.poe.ImageData
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

import com.squareup.picasso.Picasso

class FetchedImages : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImageAdapter
    private lateinit var databaseReference: DatabaseReference
    private var imagesList = mutableListOf<ImageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetched_images)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ImageAdapter(imagesList)
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("images")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                imagesList.clear()
                for (imageSnapshot in snapshot.children) {
                    val name = imageSnapshot.child("name").getValue(String::class.java) ?: ""
                    val description = imageSnapshot.child("description").getValue(String::class.java) ?: ""
                    val url = imageSnapshot.child("imageUrl").getValue(String::class.java) ?: ""
                    val rating = imageSnapshot.child("rating").getValue(Float::class.java) ?: 0f
                    val imageData = ImageData(name, description, url, rating)
                    imagesList.add(imageData)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}