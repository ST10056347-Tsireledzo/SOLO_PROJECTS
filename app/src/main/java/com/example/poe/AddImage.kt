package com.example.poe

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AddImage : AppCompatActivity() {

    private lateinit var selectButton: Button
    private lateinit var uploadButton: Button
    private lateinit var imagePreview: ImageView
    private lateinit var ratingBar: RatingBar
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var viewImagesButton: Button
    private lateinit var viewAchievementsButton: Button
    private var fileUri: Uri? = null
    private var rating: Float = 0f // Variable to store the rating

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_image)


        selectButton = findViewById(R.id.insertImagesButton)
        uploadButton = findViewById(R.id.saveButton)
        imagePreview = findViewById(R.id.imagePreview)
        ratingBar = findViewById(R.id.ratingBar)
        nameEditText = findViewById(R.id.nameEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        viewImagesButton = findViewById(R.id.viewImagesButton)


        selectButton.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select image"), 0)
        }

        uploadButton.setOnClickListener {
            if (fileUri != null) {
                uploadImageToFirebase()
            } else {
                Toast.makeText(applicationContext, "Select an image first", Toast.LENGTH_LONG)
                    .show()
            }
        }

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            // Update the rating variable when rating changes
            this.rating = rating
        }

        viewImagesButton.setOnClickListener {
            val intent = Intent(this, FetchedImages::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
                imagePreview.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Log.e("Exception", "Error", e)
            }
        }
    }

    private fun uploadImageToFirebase() {
        val name = nameEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()

        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Name and Description cannot be empty",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")
        progressDialog.setMessage("Processing...")
        progressDialog.show()

        val storageRef = FirebaseStorage.getInstance().reference.child("images")
        val imageName = UUID.randomUUID().toString()
        val imageRef = storageRef.child("$imageName.jpg")

        imageRef.putFile(fileUri!!)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Save image metadata along with the rating
                    saveImageMetadataToRealtimeDatabase(uri.toString(), name, description, rating)
                    progressDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "File uploaded successfully",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Upload failed: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun saveImageMetadataToRealtimeDatabase(
        imageUrl: String,
        name: String,
        description: String,
        rating: Float
    ) {
        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("images")
        val imageId = database.push().key ?: UUID.randomUUID().toString()
        val imageMetadata = mapOf(
            "name" to name,
            "description" to description,
            "imageUrl" to imageUrl,
            "rating" to rating, // Include the rating in the metadata
            "timestamp" to System.currentTimeMillis()
        )

        database.child(imageId).setValue(imageMetadata)
            .addOnSuccessListener {
                updateImageUploadCount()
                Toast.makeText(applicationContext, "Metadata saved successfully", Toast.LENGTH_LONG)
                    .show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    applicationContext,
                    "Error saving metadata: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun updateImageUploadCount() {
        val userImagesRef: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("user_images")
                .child("user_id") // replace "user_id" with the actual user ID
        userImagesRef.get().addOnSuccessListener { snapshot ->
            var imageCount = snapshot.getValue(Int::class.java) ?: 0
            imageCount++
            userImagesRef.setValue(imageCount)

            // Check for achievements
            checkAchievements(imageCount)
        }
    }

    private fun checkAchievements(imageCount: Int) {
        // Define achievement milestones
        val achievements = listOf(3, 5, 10)
        // Reference to the user's achievements
        val userAchievementsRef = FirebaseDatabase.getInstance().getReference("user_achievements")
            .child("user_id") // replace "user_id" with the actual user ID

        // Check if the current image count matches any of the milestones
        for (achievement in achievements) {
            if (imageCount == achievement) {
                // Show achievement unlocked message
                Toast.makeText(
                    applicationContext,
                    "Achievement unlocked: $achievement images uploaded!",
                    Toast.LENGTH_LONG
                ).show()
                // Store achievement in database
                userAchievementsRef.child(achievement.toString()).setValue(true)
            }
        }
    }
}