package com.ljb.bumscheduler.ui.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


fun Modifier.topHorizontalBorder(strokeWidth: Dp, color: Color, cornerRadiusDp: Dp) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }
        val cornerRadius = density.run { cornerRadiusDp.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = 0f, y = cornerRadius),
                strokeWidth = strokeWidthPx
            )

            // Top left arc
            drawArc(
                color = color,
                startAngle = 180f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset.Zero,
                size = Size(cornerRadius * 2, cornerRadius * 2),
                style = Stroke(width = strokeWidthPx)
            )


            drawLine(
                color = color,
                start = Offset(x = cornerRadius, y = 0f),
                end = Offset(x = width - cornerRadius, y = 0f),
                strokeWidth = strokeWidthPx
            )

            // Top right arc
            drawArc(
                color = color,
                startAngle = 270f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(x = width - cornerRadius * 2, y = 0f),
                size = Size(cornerRadius * 2, cornerRadius * 2),
                style = Stroke(width = strokeWidthPx)
            )

            drawLine(
                color = color,
                start = Offset(x = width, y = height),
                end = Offset(x = width, y = cornerRadius),
                strokeWidth = strokeWidthPx
            )
        }
    }
)