package com.example.lab2

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class SimpleViewModel : ViewModel() {

    private val _color : MutableLiveData<Color> = MutableLiveData(Color.valueOf(1f, 1f, 0f))
    val color: LiveData<Color> get() = _color

    private var _bitmap: Bitmap? = null
    val bitmap: Bitmap? get() = _bitmap

    fun pickColor() {
        with(Random.Default) {
            _color.value = Color.valueOf(nextFloat(), nextFloat(), nextFloat())
        }
    }

    fun saveBitmap(bitmap: Bitmap) {
        _bitmap = bitmap
    }
}