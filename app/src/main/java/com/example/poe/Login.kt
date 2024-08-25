package com.example.poe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.poe.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast

private lateinit var binding: ActivityLoginBinding
private lateinit var firebaseAuth: FirebaseAuth

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.link.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.login.setOnClickListener {
            val user = binding.username.text.toString().trim()
            val pass = binding.password.text.toString().trim()

            if (user.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, home_page::class.java)
                        startActivity(intent)
                        finish() // Close the login activity
                    } else {
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_LONG).show()
            }
        }
    }
}
