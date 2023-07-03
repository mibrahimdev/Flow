package io.github.mohamedisoliman.flow.ui.screens.report

import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun LineChart() {
    LineChart(data = listOf(5f, 10f, 8f, 12f, 6f, 9f), maxValue = 15f)
}

@Preview
@Composable
fun PrevBezierLineChart() {
    val strings = (0..20).map { it.toString() }

    BezierLineChart(
        data = listOf(5f, 10f, 8f, 12f, 6f, 9f),
        xLabels = strings,
        maxValue = 15f,
        yLabels = strings,
        xAxisMargin = 0f,
        yAxisMargin = 0f
    )
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

@Preview
@Composable
fun LineChartWithIndicesPreview() {
    LineChartWithIndices(
        data = listOf(5f, 10f, 8f, 12f, 6f, 9f),
        maxValue = 15f,
        horizontalIndices = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun"),
        verticalIndices = listOf(0f, 5f, 10f, 15f)
    )
}

@Composable
fun LineChartWithIndices(
    data: List<Float>,
    maxValue: Float,
    horizontalIndices: List<String>,
    verticalIndices: List<Float>
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val xStep = size.width / (data.size - 1)
        val yStep = size.height / maxValue
        val path = Path()
        path.moveTo(0f, size.height - data[0] * yStep)
        for (i in 1 until data.size) {
            path.lineTo(i * xStep, size.height - data[i] * yStep)
        }
        drawPath(path, Color.Blue, style = Stroke(width = 4f))

        // draw horizontal indices
        horizontalIndices.forEachIndexed { index, label ->
            drawLine(
                color = Color.Black,
                start = Offset(0f, index * size.height / (horizontalIndices.size - 1)),
                end = Offset(size.width, index * size.height / (horizontalIndices.size - 1)),
                strokeWidth = 2f
            )
            drawIntoCanvas {
                val textPaint = android.graphics.Paint().apply { color = Color.Black.toArgb() }
                it.nativeCanvas.drawText(
                    label,
                    0f,
                    index * size.height / (horizontalIndices.size - 1) + textPaint.textSize,
                    textPaint
                )
            }
        }

        // draw vertical indices
        verticalIndices.forEachIndexed { index, value ->
            drawLine(
                color = Color.Black,
                start = Offset(index * size.width / (verticalIndices.size - 1), 0f),
                end = Offset(index * size.width / (verticalIndices.size - 1), size.height),
                strokeWidth = 2f
            )
            drawIntoCanvas {
                val textPaint = android.graphics.Paint().apply { color = Color.Black.toArgb() }
                it.nativeCanvas.drawText(
                    value.toString(),
                    index * size.width / (verticalIndices.size - 1),
                    size.height - textPaint.textSize,
                    textPaint
                )
            }
        }
    }
}

@Composable
fun BezierLineChart(
    data: List<Float>,
    maxValue: Float,
    xLabels: List<String>,
    yLabels: List<String>,
    xAxisMargin: Float,
    yAxisMargin: Float
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val xMargin = size.width * xAxisMargin
        val yMargin = size.height * yAxisMargin
        val contentWidth = size.width - 2 * xMargin
        val contentHeight = size.height - 2 * yMargin

        val xStep = contentWidth / (data.size - 1)
        val yStep = contentHeight / maxValue

        val path = Path()
        path.moveTo(xMargin, size.height - data[0] * yStep)

        for (i in 1 until data.size) {
            val prevX = xMargin + (i - 1) * xStep
            val prevY = size.height - data[i - 1] * yStep
            val x = xMargin + i * xStep
            val y = size.height - data[i] * yStep
            val cX1 = prevX + xStep / 2
            val cY1 = prevY
            val cX2 = x - xStep / 2
            val cY2 = y
            path.cubicTo(cX1, cY1, cX2, cY2, x, y)
        }

        drawPath(path, Color.Blue, style = Stroke(width = 10f))

        // draw x labels
        val xLabelPaint = TextPaint().apply {
            color = Color.Black.toArgb()
            textSize = 24.sp.toPx()
            textAlign = android.graphics.Paint.Align.CENTER
        }
        xLabels.forEachIndexed { index, label ->
            val x = xMargin + index * xStep
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    label, x, size.height + xLabelPaint.textSize, xLabelPaint
                )
            }
        }

        // draw y labels
        val yLabelPaint = TextPaint().apply {
            color = Color.Black.toArgb()
            textSize = 24.sp.toPx()
            textAlign = android.graphics.Paint.Align.CENTER
        }
        yLabels.forEachIndexed { index, label ->
            val y = size.height - (yMargin + index * yStep)
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(label, 0f, y + yLabelPaint.textSize / 2, yLabelPaint)
            }
        }
    }
}

