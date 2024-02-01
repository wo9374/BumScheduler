package com.ljb.bumscheduler.ui.theme

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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
            val context = LocalContext.current
            //if (darkMode) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            if (darkMode) DarkColorScheme else LightColorScheme
        }

        darkMode -> DarkColorScheme
        else -> LightColorScheme
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.apply {
            setStatusBarColor(
                color = colorScheme.background,
                darkIcons = !darkMode
            )
            setNavigationBarColor(
                color = colorScheme.background,
                darkIcons = !darkMode
            )
        }
    }

    //스테이터스 바, 시스템 네비게이션 바 의 기본 Text, Icon Color 변경을 위해 accompanist-systemuicontroller 사용
    /*val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = colorScheme.primary.toArgb()

            val insetController = WindowCompat.getInsetsController(window, view)
            insetController.isAppearanceLightStatusBars = darkMode
            insetController.isAppearanceLightNavigationBars = darkMode
        }
    }*/

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}