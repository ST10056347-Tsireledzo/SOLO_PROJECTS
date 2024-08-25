package com.example.poe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.poe.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.link.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        binding.register.setOnClickListener {
            val user = binding.username.text.toString()
            val name = binding.name.text.toString()
            val pass = binding.password.text.toString()

            if (user.isNotEmpty() && name.isNotEmpty()  && pass.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show()
            }
        }
    }
}