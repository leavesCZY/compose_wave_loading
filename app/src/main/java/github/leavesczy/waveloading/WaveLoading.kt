package github.leavesczy.waveloading

import android.graphics.Typeface
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit

/**
 * @Author: leavesCZY
 * @Date: 2022/2/16 15:39
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
@Composable
fun WaveLoading(
    modifier: Modifier,
    text: String,
    textSize: TextUnit,
    waveColor: Color,
    downTextColor: Color = Color.White,
    animationSpec: InfiniteRepeatableSpec<Float> = infiniteRepeatable(
        animation = tween(
            durationMillis = 500,
            easing = CubicBezierEasing(0.2f, 0.2f, 0.7f, 0.9f)
        ),
        repeatMode = RepeatMode.Restart
    )
) {
    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current.density
        val circleSizeDp = minOf(maxWidth, maxHeight)
        val circleSizePx = circleSizeDp.value * density
        val waveWidth = circleSizePx / 1.2f
        val waveHeight = circleSizePx / 10f
        val textPaint by remember {
            mutableStateOf(Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                isDither = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = android.graphics.Paint.Align.CENTER
            })
        }
        val wavePath by remember {
            mutableStateOf(Path())
        }
        val circlePath by remember {
            mutableStateOf(Path().apply {
                addArc(
                    Rect(0f, 0f, circleSizePx, circleSizePx),
                    0f, 360f
                )
            })
        }
        val animateValue by rememberInfiniteTransition(label = "").animateFloat(
            initialValue = 0f,
            targetValue = waveWidth,
            animationSpec = animationSpec,
            label = "",
        )
        Canvas(modifier = modifier.requiredSize(size = circleSizeDp)) {
            drawTextToCenter(
                text = text,
                textPaint = textPaint,
                textSize = textSize.toPx(),
                textColor = waveColor.toArgb()
            )
            wavePath.reset()
            wavePath.moveTo(-waveWidth + animateValue, circleSizePx / 2.3f)
            var i = -waveWidth
            while (i < circleSizePx + waveWidth) {
                wavePath.relativeQuadraticBezierTo(waveWidth / 4f, -waveHeight, waveWidth / 2f, 0f)
                wavePath.relativeQuadraticBezierTo(waveWidth / 4f, waveHeight, waveWidth / 2f, 0f)
                i += waveWidth
            }
            wavePath.lineTo(circleSizePx, circleSizePx)
            wavePath.lineTo(0f, circleSizePx)
            wavePath.close()
            val resultPath = Path.combine(
                path1 = circlePath,
                path2 = wavePath,
                operation = PathOperation.Intersect
            )
            drawPath(path = resultPath, color = waveColor)
            clipPath(path = resultPath, clipOp = ClipOp.Intersect) {
                drawTextToCenter(
                    text = text,
                    textPaint = textPaint,
                    textSize = textSize.toPx(),
                    textColor = downTextColor.toArgb()
                )
            }
        }
    }
}

private fun DrawScope.drawTextToCenter(
    text: String,
    textPaint: android.graphics.Paint,
    textSize: Float,
    textColor: Int
) {
    textPaint.textSize = textSize
    textPaint.color = textColor
    val fontMetrics = textPaint.fontMetrics
    val x = size.width / 2f
    val y = size.height / 2f - (fontMetrics.top + fontMetrics.bottom) / 2f
    drawContext.canvas.nativeCanvas.drawText(text, x, y, textPaint)
}