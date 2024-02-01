package com.ljb.bumscheduler.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

fun Modifier.bgBorder(strokeWidth: Dp, cornerRadiusDp: Dp, enabled: Boolean) = composed {
    this.then(
        if (enabled) {
            border(
                width = strokeWidth,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(cornerRadiusDp)
            )
        } else {
            this
        }
    )
}

fun Modifier.bgToday(enabled: Boolean, color: Color) = this.then(
    if (enabled) {
        background(color)
    } else {
        this
    }
)

//Modifier onClick 클릭 효과 제거
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        //enabled = enabled,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick.invoke()
    }
}

// 상단 좌우측 Radius Border
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