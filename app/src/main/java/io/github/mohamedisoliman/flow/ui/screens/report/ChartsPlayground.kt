package io.github.mohamedisoliman.flow.ui.screens.report

import android.graphics.Paint
import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
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
fun LineChart() {
    val data = listOf(5f, 10f, 8f, 12f, 6f, 9f)
    LineChart(data = data, maxValue = data.max() + 5)
}

@Preview
@Composable
fun PrevBezierChart() {
    val points = mapOf(
        10f to 1f,
        11f to 0.25f,
        12f to 0.5f,
        13.5f to 0.75f,
        16f to .5f,
        18f to 2f,
    )
    BezierChart(points = points)

}

@Composable
fun LineChart(data: List<Float>, maxValue: Float) {

    Canvas(modifier = Modifier.fillMaxSize()) {

        val xStep = size.width / (data.size - 1)

        val yStep = size.height / maxValue
        val path = Path()

        path.moveTo(0f, size.height - data[0] * yStep)

        for (i in 1 until data.size) {
            path.lineTo(i * xStep, size.height - data[i] * yStep)
        }
        drawPath(path, Color.Blue, style = Stroke(width = 4f))

    }
}

@Composable
fun HoursBezierChart(dataPoints: List<Float>) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val path = Path()
        val startY = size.height * (1f - dataPoints.first())

        path.moveTo(0f, startY)

        for (i in 1 until dataPoints.size step 2) {
            val controlPointX = size.width * (i.toFloat() / (dataPoints.size - 1))
            val controlPointY = size.height * (1 - dataPoints[i])
            val endPointX = size.width * ((i + 1).toFloat() / (dataPoints.size - 1))
            val endPointY = size.height * (1 - dataPoints[i + 1])

            path.quadraticBezierTo(
                controlPointX, controlPointY, endPointX, endPointY
            )
        }

        drawPath(
            path = path, color = Color.Cyan, style = Stroke(width = 4.dp.toPx())
        )

        val hourLabels = List(24) { hour -> "$hour:00" }

        val labelPaint = Paint().apply {
            color = Color.Black.toArgb()
            textSize = 16f
        }

        val fontMetrics = labelPaint.fontMetrics
        val labelBaseline = size.height + fontMetrics.bottom

        hourLabels.forEachIndexed { index, label ->
            val x = (size.width / 23f) * index

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    label, x, labelBaseline, labelPaint
                )
            }

        }
    }
}

@Composable
fun BezierChart(
    modifier: Modifier = Modifier,
    lineColor: Color = Purple500,
    lineWidth: Dp = 20.dp,
    points: Map<Float, Float> = emptyMap(),
) {

    val xData = points.keys.toFloatArray()
    val yData = points.values.toFloatArray()

    val xAxisHours = 24

    val yMax = remember { yData.max() }
    val yMin = remember { yData.min() }

    val textColor = MaterialTheme.colors.surface.toArgb()

    val linearGradient = Brush.linearGradient(
        colors = listOf(Color.White, lineColor),
        end = Offset(0f, Float.POSITIVE_INFINITY),
        start = Offset(Float.POSITIVE_INFINITY, 0f)
    )


    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        val xStep = size.width / points.size
        val yStep = size.height / (yMax - yMin)

        val path = Path()

        val textPaint = TextPaint().apply {
            color = textColor
            textSize = 24.sp.toPx()
            textAlign = Paint.Align.CENTER
        }

        path.moveTo(xStep, size.height - yData[0] * yStep)

        for (i in 1 until yData.size) {
            val prevX = (i - 1) * xStep + xStep //
            val prevY = size.height - yData[i - 1] * yStep
            val x = i * xStep + xStep//
            val y = size.height - yData[i] * yStep
            val cX1 = prevX + xStep / 2
            val cX2 = x - xStep / 2
            path.cubicTo(cX1, prevY, cX2, y, x, y)
        }

        drawPath(path, linearGradient, style = Stroke(width = lineWidth.value))

        //x-labels
        (xData.indices step 1).forEach { i ->
            val hour = xData[i]//y
            val x = i * xStep
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    hour.toString(), x + xStep, size.height, textPaint
                )
            }

            drawLine(
                color = Color.White,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
        }

        //y-labels
        val yLevels = 7
        val priceStep = (yMax - yMin) / yLevels
        (0..yLevels).forEach { i ->
            if (i == 0) return@forEach

            val y = size.height - i * (size.height / yLevels)

            drawLine(
                color = Color.White,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    (priceStep * i).toString(), 0f, y, textPaint
                )
            }
        }

    }
}

@Preview
@Composable
fun LineChartPrev() {
    val data = listOf(
        Pair(6, 111.45),
        Pair(7, 111.0),
        Pair(8, 113.45),
        Pair(9, 112.25),
        Pair(10, 116.45),
        Pair(11, 113.35),
        Pair(12, 118.65),
        Pair(13, 110.15),
        Pair(14, 113.05),
        Pair(15, 114.25),
        Pair(16, 116.35),
        Pair(17, 117.45),
        Pair(18, 112.65),
        Pair(19, 115.45),
        Pair(20, 111.85)
    )

    LineChart(
        data = data, modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}

@Composable
fun LineChart(
    data: List<Pair<Int, Double>> = emptyList(), modifier: Modifier = Modifier
) {
    val graphColor = Color.Cyan
    val transparentGraphColor = remember { graphColor.copy(alpha = 0.5f) }
    val upperValue = remember { (data.maxOfOrNull { it.second }?.plus(1))?.roundToInt() ?: 0 }
    val lowerValue = remember { (data.minOfOrNull { it.second }?.toInt() ?: 0) }
    val density = LocalDensity.current

    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Canvas(modifier = modifier) {
        val spacing = 100f


        val spacePerHour = (size.width - spacing) / data.size

        (data.indices step 2).forEach { i ->
            val hour = data[i].first   //y
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    hour.toString(), spacing + i * spacePerHour, size.height, textPaint
                )
            }
        }

        //y-labels
        val priceStep = (upperValue - lowerValue) / 5f
        (0..4).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    (lowerValue + priceStep * i).roundToInt().toString(),
                    30f,
                    size.height - spacing - i * size.height / 5f,
                    textPaint
                )
            }
        }

        val strokePath = Path().apply {
            val height = size.height
            data.indices.forEach { i ->
                val info = data[i]
                val ratio = (info.second - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (ratio * height).toFloat()

                if (i == 0) {
                    moveTo(x1, y1)
                }
                lineTo(x1, y1)
            }
        }

        drawPath(
            path = strokePath, color = graphColor, style = Stroke(
                width = 2.dp.toPx(), cap = StrokeCap.Round
            )
        )

        val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
            lineTo(size.width - spacePerHour, size.height - spacing)
            lineTo(spacing, size.height - spacing)
            close()
        }

        drawPath(
            path = fillPath, brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor, Color.Transparent
                ), endY = size.height - spacing
            )
        )

    }
}

