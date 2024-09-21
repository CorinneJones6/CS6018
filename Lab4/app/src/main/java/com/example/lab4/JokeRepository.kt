package com.example.lab4

import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date

class JokeRepository(private val scope: CoroutineScope, private val dao: JokeDAO) {

    val currentJoke = dao.currentJoke().asLiveData()
    val allJokes = dao.allJokes().asLiveData()


    fun addJoke(joke: String) {
        scope.launch {
            dao.addJokeData(
                JokeData(Date(), joke)
            )
        }
    }
}