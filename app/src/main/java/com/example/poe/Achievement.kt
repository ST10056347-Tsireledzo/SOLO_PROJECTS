package com.example.poe

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class Achievement : AppCompatActivity() {

    private lateinit var achievementTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressTextView: TextView
    private lateinit var database: DatabaseReference
    private val milestones = intArrayOf(1, 3, 10)
    private val milestoneMessages = mapOf(
        1 to "Starter! You've uploaded 1 destinations!",
        3 to "Collector! You've uploaded 3 destinations!",
        10 to "Packrat! You've uploaded 10 destinations!"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement)

        achievementTextView = findViewById(R.id.achievementTextView)
        progressBar = findViewById(R.id.progressBar)
        progressTextView = findViewById(R.id.progressTextView)

        database = FirebaseDatabase.getInstance().getReference("user_images").child("user_id") // replace "user_id" with the actual user ID

        getUserImageUploadCount()
    }

    private fun getUserImageUploadCount() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val imageCount = snapshot.getValue(Int::class.java) ?: 0
                updateProgress(imageCount)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to fetch data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateProgress(currentProgress: Int) {
        progressBar.max = milestones.last()
        progressBar.progress = currentProgress
        val progressPercentage = (currentProgress * 100) / milestones.last()
        progressTextView.text = "Progress: $progressPercentage%"

        val achievementsText = if (currentProgress >= milestones.last()) {
            "Congratulations! You've achieved your goal!"
        } else {
            "Keep going! You need ${milestones.last() - currentProgress} more destinations to reach your goal.\n\nAchievements unlocked:\n"
        }

        // Fetch and display individual achievements
        displayAchievements(achievementsText, currentProgress)
    }

    private fun displayAchievements(achievementsText: String, currentProgress: Int) {
        val userAchievementsRef = FirebaseDatabase.getInstance().getReference("user_achievements").child("user_id") // replace "user_id" with the actual user ID
        userAchievementsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val achievementsTextBuilder = StringBuilder(achievementsText)
                for (milestone in milestones) {
                    if (currentProgress >= milestone) {
                        achievementsTextBuilder.append(milestoneMessages[milestone]).append("\n")
                        userAchievementsRef.child(milestone.toString()).setValue(true)
                    }
                }
                achievementTextView.text = achievementsTextBuilder.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to fetch achievements: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
