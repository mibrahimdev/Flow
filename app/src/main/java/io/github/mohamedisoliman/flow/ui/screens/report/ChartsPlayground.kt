package io.github.mohamedisoliman.flow.ui.screens.report

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.mohamedisoliman.flow.ui.theme.Purple500
import kotlin.math.roundToInt

@Preview
@Composable
fun PrevCustomBezier() {
    val data = mapOf(
        Pair(13f, 2f),
        Pair(14f, 4f),
        Pair(15f, 2.4f),
        Pair(16f, 1f),
        Pair(19f, 2f),
        Pair(20f, 0.25f)
    )

    BezierChart(
        data = data, modifier = Modifier
            .fillMaxWidth()
            .height(500.dp) //todo: revisit
    )
}

@Preview
@Composable
fun PrevChartTwo() {

    val points = mapOf(
        10f to 9f,
        11f to 3.5f,
        12f to 4f,
        16f to 2.5f,
        18f to 2f,
    )

    BezierChart(
        data = points, modifier = Modifier
            .fillMaxWidth()
            .height(500.dp) //todo: revisit
    )
}

@Composable
fun BezierChart(
    modifier: Modifier = Modifier,
    data: Map<Float, Float> = emptyMap(),
    lineColor: Color = Purple500,
    lineWidth: Dp = 30.dp,
) {

    val spacing = 100f
    val yMax = data.values.maxOrNull()
    val yMin = data.values.minOrNull()

    val yUpperValue = remember { (yMax?.plus(1))?.roundToInt() ?: 0 }
    val yLowerValue = remember { (yMin?.toInt() ?: 0) }

    val density = LocalDensity.current

    var (xMaxPoint, yMaxPoint) = 0f to 0f

    val colors = MaterialTheme.colors

    val textPaint = remember(density) {
        Paint().apply {
            color = colors.onSecondary.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    val horizontalDashPathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 30f), 0f)
    val verticalDashPathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 30f), 0f)


    Canvas(
        modifier = modifier.then(
            Modifier

                .background(
                    colors.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))

        ).padding(24.dp)
    ) {

        val spacePerHour = (size.width - spacing) / data.size

        //x-labels
        data.entries.forEachIndexed { index, entry ->
            val xLabel = spacing + index * spacePerHour
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    entry.key.toString(), xLabel, size.height, textPaint
                )
            }

            if (entry.value == yMax) xMaxPoint = xLabel
        }

        val levels = (size.height / (yUpperValue - yLowerValue)).roundToInt()

        //y-labels
        for (i in 0 until yUpperValue) {
            val yLevel = size.height - spacing - i * levels
            val yText = i * levels * (yUpperValue - yLowerValue).toFloat() / size.height

            drawContext.canvas.nativeCanvas.apply {
                drawText(yText.roundToInt().toString(), 30f, yLevel, textPaint)

                drawLine(
                    color = Color.LightGray,
                    start = Offset(spacing, yLevel),
                    end = Offset(width - spacing, yLevel),
                    pathEffect = horizontalDashPathEffect
                )
            }
        }

        //graph
        val strokePath = Path().apply {
            val height = size.height
            val hourValues = data.entries.sortedBy { it.key }

            hourValues.forEachIndexed { index, (_, value) ->
                val x = spacing + index * spacePerHour
                val y =
                    height - spacing - (size.height * value / yUpperValue) // Px = PxMax * value / ValueMax

                if (index == 0) {
                    moveTo(x, y)
                } else {
                    val prevX = spacing + (index - 1) * spacePerHour
                    val prevY =
                        height - spacing - (size.height * hourValues[index - 1].value / yUpperValue)

                    val cX1 = prevX + spacePerHour / 2
                    val cX2 = x - spacePerHour / 2

                    cubicTo(cX1, prevY, cX2, y, x, y)
                }

                if (yMax == value) {
                    yMaxPoint = y
                }

            }
        }

        val rectWidth = 200f

        val roadGradient = Brush.linearGradient(
            colors = listOf(Color.White,Color.White, Color.Black),
            end = Offset(xMaxPoint, yMaxPoint + lineWidth.value),
            start = Offset(xMaxPoint, size.height - spacing),
        )

        val linearGradient = Brush.horizontalGradient(
            colors = listOf(lineColor, lineColor.copy(alpha = 0.5f), Color.White),
            startX = 0f,
        )



        drawRect(
            brush = roadGradient,
            topLeft = Offset(xMaxPoint - rectWidth / 2, 0f),
            size = Size(rectWidth, size.height - spacing),
            alpha = 0.10f
        )

        drawLine(
            color = Color.White,
            start = Offset(xMaxPoint, yMaxPoint + lineWidth.value),
            end = Offset(xMaxPoint, size.height - spacing),
            pathEffect = verticalDashPathEffect,
            strokeWidth = Stroke.DefaultMiter
        )

        drawPath(strokePath, linearGradient, style = Stroke(width = lineWidth.value))



        drawCircle(
            brush = linearGradient,
            radius = lineWidth.value,
            center = Offset(xMaxPoint, yMaxPoint),
            style = Stroke(width = lineWidth.value)
        )

        drawCircle(
            color = Color.White,
            radius = lineWidth.value / 2,
            center = Offset(xMaxPoint, yMaxPoint),
        )

    }

}

fun List<Float>.calculateUnitStep(): Float? {
    if (size < 2) return null

    val sortedNumbers = sorted()
    var unitStep: Float? = null

    for (i in 1 until sortedNumbers.size) {
        val diff = sortedNumbers[i] - sortedNumbers[i - 1]
        if (diff != 0f && (unitStep == null || diff < unitStep)) unitStep = diff
    }

    return unitStep
}


