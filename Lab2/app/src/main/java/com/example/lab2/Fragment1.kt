package com.example.lab2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.lab2.databinding.Fragment1Binding

class Fragment1 : Fragment() {

    private var buttonFunction : () -> Unit = {}

    fun setButtonFunction(newFunc: ()-> Unit) {
        buttonFunction = newFunc
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = Fragment1Binding.inflate(inflater, container, false)
        val viewModel : SimpleViewModel by activityViewModels()

        binding.clickMe.setOnClickListener{
            viewModel.pickColor()
            buttonFunction()
        }
        return binding.root
    }

}