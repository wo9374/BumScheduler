package com.ljb.bumscheduler.ui.screen

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapScreen() {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

        }

        var mapProperties by remember {
            mutableStateOf(
                MapProperties(maxZoom = 10.0, minZoom = 5.0)
            )
        }
        var mapUiSettings by remember {
            mutableStateOf(
                MapUiSettings(isLocationButtonEnabled = false)
            )
        }

        NaverMap(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun AnimatedVerticalGradient() {
    val endColor =  remember {
        Animatable(Color.Blue)
    }
    val brush = Brush.verticalGradient(listOf(Color.Red, endColor.value))

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = brush)
    ) {
        Button(
            onClick = {
                scope.launch {
                    endColor.animateTo(
                        targetValue = Color.Red,
                        animationSpec = tween(
                            durationMillis = 1000,
                            easing = LinearOutSlowInEasing
                        )
                    )
                }
            }
        ) {
            // Button 내용
        }
    }
}