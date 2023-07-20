package io.github.mohamedisoliman.flow.ui.screens.report

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
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

    CustomBezierChart(
        data = data, modifier = Modifier
            .fillMaxWidth()
            .height(800.dp) //todo: revisit
    )
}

@Preview
@Composable
fun PrevChartTwo() {

    val points = mapOf(
        10f to 60f,
        11f to 15f,
        12f to 15f,
        16f to 2.5f,
        18f to 2f,
    )

    CustomBezierChart(
        data = points, modifier = Modifier
            .fillMaxWidth()
            .height(800.dp) //todo: revisit
    )
}

@Composable
fun CustomBezierChart(
    modifier: Modifier = Modifier,
    data: Map<Float, Float> = emptyMap(),
    lineColor: Color = Purple500,
    lineWidth: Dp = 30.dp,
    maxLevels: Int = 5,
    scale: Int = 1
) {

    val spacing = 100f
    val yMax = data.values.maxOrNull()
    val yMin = data.values.minOrNull()

    val yUpperValue = remember { (yMax?.plus(1))?.roundToInt() ?: 0 }
    val yLowerValue = remember { (yMin?.toInt() ?: 0) }

    val yUnitStep = data.values.toList().calculateUnitStep() ?: 1f

    val density = LocalDensity.current

    var (xMaxPoint, yMaxPoint) = 0f to 0f

    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    val horizontalDashPathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 30f), 0f)
    val verticalDashPathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 30f), 0f)


    Canvas(modifier = modifier) {

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

        val priceStep = (yUpperValue - yLowerValue) / maxLevels.toFloat()
        val step = yUpperValue / yUnitStep
        //y-labels
        (0 until maxLevels).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                val yLevel = size.height - spacing - i * size.height / maxLevels //

                drawText(
                    (yLowerValue + step * i).roundToInt().toString(), 30f, yLevel, textPaint
                )


                drawLine(
                    color = Color.LightGray,
                    start = Offset(60f, yLevel),
                    end = Offset(height - spacing, yLevel),
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
                    height - spacing - ((value - yLowerValue) / (yUpperValue - yLowerValue) * height)

                if (index == 0) {
                    moveTo(x, y)
                } else {
                    val prevX = spacing + (index - 1) * spacePerHour
                    val prevY =
                        height - spacing - ((hourValues[index - 1].value - yLowerValue) / (yUpperValue - yLowerValue) * height).toFloat()

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
            colors = listOf(Color.White, lineColor),
            end = Offset(xMaxPoint, yMaxPoint + lineWidth.value),
            start = Offset(xMaxPoint, size.height - spacing),
        )

        val linearGradient = Brush.horizontalGradient(
            colors = listOf(lineColor, lineColor, Color.White),
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


