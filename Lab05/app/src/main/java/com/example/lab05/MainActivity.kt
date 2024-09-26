package com.example.lab05

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab05.ui.theme.Lab05Theme
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val grav = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        val gravFlow : Flow<FloatArray>? = grav?.let { getGravData(it, sensorManager) }

        setContent {
            Lab05Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    drawScreen(gravFlow)
                }
            }
        }
    }
}

@Composable
fun drawScreen(gravFlow: Flow<FloatArray>?, modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.height(50.dp))
    Row(modifier = modifier) {
        var textGrav by remember { mutableStateOf("") }

        gravFlow?.let {
            LaunchedEffect(key1 = it) {
                it.collect { gravReading ->
                    textGrav = "Grav reading: ${gravReading[0]} ${gravReading[1]} ${gravReading[2]}"
                }
            }
        }
        Text(text = textGrav)
    }
}

fun getGravData(gravSensor: Sensor, sensorManager: SensorManager): Flow<FloatArray> {
    return channelFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    channel.trySend(event.values).isSuccess
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, gravSensor, SensorManager.SENSOR_DELAY_NORMAL)

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}