package com.example.poe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class home_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)


        val homebut = findViewById<ImageView>(R.id.image_view1)
        homebut.setOnClickListener{ val intent = Intent(this,AddImage::class.java)
            startActivity(intent)

        }

        val galbutton = findViewById<ImageView>(R.id.image_view20)
        galbutton.setOnClickListener{ val intent = Intent(this,FetchedImages::class.java)
            startActivity(intent)

        }
        val achieve = findViewById<ImageView>(R.id.image_view21)
        achieve.setOnClickListener{ val intent = Intent(this, Achievement::class.java)
            startActivity(intent)

        }
        val suggest = findViewById<ImageView>(R.id.image_view22)
        suggest.setOnClickListener{ val intent = Intent(this, Suggestion::class.java)
            startActivity(intent)

        }
        val wish = findViewById<ImageView>(R.id.image_view23)
        wish.setOnClickListener{ val intent = Intent(this, Wishlist::class.java)
            startActivity(intent)

        }
        val exit = findViewById<ImageView>(R.id.image_view)
        exit.setOnClickListener{ val intent = Intent(this, Login::class.java)
            startActivity(intent)

        }

    }
}