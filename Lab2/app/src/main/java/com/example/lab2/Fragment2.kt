package com.example.lab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lab2.databinding.Fragment2Binding

class Fragment2 : Fragment() {
    private var _binding: Fragment2Binding? = null
    private val binding get() = _binding!!

    private val viewModel: SimpleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = Fragment2Binding.inflate(inflater, container, false)

        if (viewModel.bitmap != null) {
            binding.myView.restoreBitmap(viewModel.bitmap!!)
        } else {
            viewModel.color.observe(viewLifecycleOwner) { color ->
                binding.myView.drawCircle(color)
            }
        }

        binding.myView.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                viewModel.pickColor()
                view.performClick()
                true
            } else {
                false
            }
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()

        viewModel.saveBitmap(binding.myView.getBitmap())
    }
}