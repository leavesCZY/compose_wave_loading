package github.leavesczy.waveloading

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
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
            durationMillis = 600,
            easing = CubicBezierEasing(0.111f, 0.333f, 0.777f, 0.888f)
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
        val wavePath = remember {
            Path()
        }
        val circlePath = remember {
            Path().apply {
                addArc(
                    Rect(0f, 0f, circleSizePx, circleSizePx),
                    0f, 360f
                )
            }
        }
        val animateValue by rememberInfiniteTransition(label = "").animateFloat(
            initialValue = 0f,
            targetValue = waveWidth,
            animationSpec = animationSpec,
            label = "",
        )
        val textMeasurer = rememberTextMeasurer()
        Canvas(
            modifier = modifier
                .requiredSize(size = circleSizeDp)
        ) {
            drawTextToCenter(
                textMeasurer = textMeasurer,
                text = text,
                textSize = textSize,
                textColor = waveColor
            )
            wavePath.reset()
            wavePath.moveTo(-waveWidth + animateValue, circleSizePx / 2.2f)
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
                    textMeasurer = textMeasurer,
                    text = text,
                    textSize = textSize,
                    textColor = downTextColor
                )
            }
        }
    }
}

private fun DrawScope.drawTextToCenter(
    textMeasurer: TextMeasurer,
    text: String,
    textSize: TextUnit,
    textColor: Color
) {
    val textLayoutResult = textMeasurer.measure(
        text = text,
        style = TextStyle(
            color = textColor,
            fontSize = textSize,
            textAlign = TextAlign.Center
        )
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(
            x = (size.width - textLayoutResult.size.width) / 2,
            y = (size.height - textLayoutResult.size.height) / 2
        )
    )
}