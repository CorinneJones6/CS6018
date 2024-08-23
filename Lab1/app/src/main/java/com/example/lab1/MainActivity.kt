package com.example.lab1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lab1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener {
            newActivity(binding.button1.text.toString())
        }

        binding.button2.setOnClickListener {
            newActivity(binding.button2.text.toString())
        }

        binding.button3.setOnClickListener {
            newActivity(binding.button3.text.toString())
        }

        binding.button4.setOnClickListener {
            newActivity(binding.button4.text.toString())
        }

        binding.button5.setOnClickListener {
            newActivity(binding.button5.text.toString())
        }
    }

    private fun newActivity(message: String) {
        val messageIntent = Intent(this, MainActivity2::class.java)
        val messageBundle = Bundle()
        messageBundle.putString("message", message)
        messageIntent.putExtras(messageBundle)
        startActivity(messageIntent)
    }
}