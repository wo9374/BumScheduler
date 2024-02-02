package com.ljb.bumscheduler.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.ljb.bumscheduler.viewmodel.SettingViewModel
import com.ljb.data.repository.DARK_MODE
import com.ljb.data.repository.LIGHT_MODE

private val LightColorScheme = lightColorScheme(
    primary =  Color.White,
    onPrimary = Color.Black,

    background = Color.White,
    onBackground = Color.Black,

    surface = Color.White,
    onSurface = Color.Black,
    onSurfaceVariant = Color.Black,

    secondaryContainer = White95
)


private val DarkColorScheme = darkColorScheme(
    primary = Black10,
    onPrimary = Color.White,

    background = Color.Black,
    onBackground = Color.White,

    surface = Black10,
    onSurface = Color.White,
    onSurfaceVariant = Color.White,

    secondaryContainer = Black10
)

@Composable
fun BumSchedulerTheme(
    settingViewModel: SettingViewModel = hiltViewModel(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkModeState by settingViewModel.darkModeState.collectAsState()

    val darkMode = when(darkModeState){
        LIGHT_MODE -> false
        DARK_MODE -> true
        else -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            //val context = LocalContext.current
            //if (darkMode) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            if (darkMode) DarkColorScheme else LightColorScheme
        }

        darkMode -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val bgColor = colorScheme.background.toArgb()

            val window = (view.context as Activity).window.apply {
                statusBarColor = bgColor
                navigationBarColor = bgColor
            }

            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkMode
                isAppearanceLightNavigationBars = !darkMode
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}