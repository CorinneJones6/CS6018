package com.example.lab1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lab1.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val theExtrasBundle = intent.extras!!
        val displayMessage = theExtrasBundle.getString("message")

        binding.textView.text = displayMessage
    }
}