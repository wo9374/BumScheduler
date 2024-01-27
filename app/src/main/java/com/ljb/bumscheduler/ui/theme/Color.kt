package com.ljb.bumscheduler.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val DefaultRed = Color(0xFFff4d4d)
val DefaultBlue = Color(0xFF1a75ff)
val DefaultGreen = Color(0xFF009933)

fun defaultTxtColor(darkTheme: Boolean) = if (darkTheme) Color.White else Color.Black
fun reverseTxtColor(darkTheme: Boolean) = if (darkTheme) Color.Black else Color.White
fun grayColor(darkTheme: Boolean) = if (darkTheme) Color.LightGray else Color.DarkGray

fun grayAlpha3(darkTheme: Boolean) = if (darkTheme) Color.LightGray.copy(alpha = 0.3f) else Color.DarkGray.copy(alpha = 0.3f)
fun grayAlpha6(darkTheme: Boolean) = if (darkTheme) Color.LightGray.copy(alpha = 0.6f) else Color.DarkGray.copy(alpha = 0.6f)