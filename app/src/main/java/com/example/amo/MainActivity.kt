package com.example.amo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val firstButton = findViewById<Button>(R.id.firstLabButton)
        val secondLabButton = findViewById<Button>(R.id.secondLabOpenButton)
        val thirdLabButton = findViewById<Button>(R.id.thirdLabOpenButton)
        val forthLabButton = findViewById<Button>(R.id.forthLabOpenButton)

        firstButton.setOnClickListener {
            intent = Intent(this, FirstLab::class.java)
            startActivity(intent)
        }

        secondLabButton.setOnClickListener{
            intent = Intent(this, SecondLab::class.java)
            startActivity(intent)
        }

        thirdLabButton.setOnClickListener{
            intent = Intent(this, ThirdLab::class.java)
            startActivity(intent)
        }

        forthLabButton.setOnClickListener{
            intent = Intent(this, ForthLab::class.java)
            startActivity(intent)
        }

        fifthLabOpenButton.setOnClickListener{
            intent = Intent(this, FifthLab::class.java)
            startActivity(intent)
        }
    }
}
