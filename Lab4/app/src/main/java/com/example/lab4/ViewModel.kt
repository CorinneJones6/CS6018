package com.example.lab4

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModel : ViewModel() {
    private val _jokes = MutableLiveData<List<String>>(emptyList())
    val jokes: LiveData<List<String>> = _jokes

    private val _latestJoke = MutableLiveData("No jokes yet!")
    val latestJoke: LiveData<String> = _latestJoke

    fun addJoke(joke: String) {
        _latestJoke.value = joke
        _jokes.value = _jokes.value?.plus(joke)
    }
}