package com.example.lab05

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.platform.LocalDensity
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
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val maxWidthPx = constraints.maxWidth.toFloat()
        val maxHeightPx = constraints.maxHeight.toFloat()
        val marbleSizeDp = 50.dp
        val density = LocalDensity.current
        val marbleSizePx = with(density) { marbleSizeDp.toPx() }
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        gravFlow?.let {
            LaunchedEffect(key1 = it) {
                it.collect { gravReading ->
                    offsetX += -gravReading[0] * 10
                    offsetY += gravReading[1] * 10

                    offsetX = offsetX.coerceIn(0f, maxWidthPx - marbleSizePx)
                    offsetY = offsetY.coerceIn(0f, maxHeightPx - marbleSizePx)
                }
            }
        }

        val offsetXDp: Dp = with(density) { offsetX.toDp() }
        val offsetYDp: Dp = with(density) { offsetY.toDp() }

        Box(
            modifier = Modifier
                .offset(x = offsetXDp, y = offsetYDp)
                .background(color = Color.Blue, shape = CircleShape)
                .height(marbleSizeDp)
                .width(marbleSizeDp)
        )
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