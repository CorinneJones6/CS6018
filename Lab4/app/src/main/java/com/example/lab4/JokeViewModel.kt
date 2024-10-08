package com.example.lab4

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class JokeViewModel(private val repository: JokeRepository) : ViewModel() {
    val currentJoke: LiveData<JokeData> = repository.currentJoke

    val allJokes: LiveData<List<JokeData>> = repository.allJokes

    fun addJoke(joke: Joke) {
        viewModelScope.launch {
            repository.addJoke(joke.value)
        }
    }
}

    // This factory class allows us to define custom constructors for the view model
    class JokeViewModelFactory(private val repository: JokeRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JokeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return JokeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}