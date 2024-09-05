package com.example.lab2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import com.example.lab2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        binding.fragmentContainerView.getFragment<Fragment1>().setButtonFunction {
            val fragment2 = Fragment2()
            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment2, "frag_2")
            transaction.addToBackStack(null)
            transaction.commit()
        }
        setContentView(binding.root)
    }
}