package com.example.lab4

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lab4.ui.theme.Lab4Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.Gson
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: JokeViewModel by viewModels{ JokeViewModelFactory((application as JokeApplication).jokeRepository) }
        setContent {
            Lab4Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JokeScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun JokeScreen(viewModel: JokeViewModel) {
    val coroutineScope = rememberCoroutineScope()

    val latestJoke by viewModel.currentJoke.observeAsState()
    val jokes by viewModel.allJokes.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    val jokeStr = fetchJoke()
                    val jokeData = Joke(jokeStr)
                    viewModel.addJoke(jokeData)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text("Get a Joke")
        }

        Text(
            text = "========== Newest Joke ==========",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = latestJoke?.joke ?: "No joke available",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Start
        )

        Text(
            text = "========== Old Jokes ==========",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(jokes.reversed()) { jokeData ->
                Text(
                    text = jokeData.joke,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

suspend fun fetchJoke(): String {
    return withContext(Dispatchers.IO) {
        val builder = Uri.Builder()
        builder.scheme("https")
            .authority("api.chucknorris.io")
            .appendPath("jokes")
            .appendPath("random").build()
        val myUrl = builder.build().toString()
        val connection = URL(myUrl).openConnection() as HttpURLConnection

        try {
            connection.connect()
            val gson = Gson()
            val inputStreamReader = InputStreamReader(connection.inputStream, "UTF-8")
            val response = gson.fromJson(inputStreamReader, Joke::class.java)
            response.value
        } catch (e: Exception) {
            e.printStackTrace()
            "Joke retrieval failed"
        }
    }
}

data class Joke(val value: String)