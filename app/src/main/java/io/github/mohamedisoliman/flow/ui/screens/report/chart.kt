package io.github.mohamedisoliman.flow.ui.screens.report

import android.graphics.Paint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.mohamedisoliman.flow.ui.theme.Figma

data class Axis(
    val max: Int = 1,
    val skipRate: Int = 1,
    val labels: List<String> = emptyList(),
)

@Preview(showBackground = true)
@Composable
fun PreviewLineChart() {

    val horizontalLabels = (0 until 10).map { it.toString() }
    val verticalLabels = (0 until 10).map { it.toString() }

    val points = mapOf(
        10f to 1f,
        11f to .25f,
        12f to .5f,
        16f to .5f,
        18f to 2f,
    )

    LineChart(
        points = points,
        horizontalAxis = Axis(
            20, 0, horizontalLabels
        ),
        verticalAxis = Axis(
            20, 0, verticalLabels
        ),
    )
}

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    points: Map<Float, Float> = emptyMap(),
    horizontalAxis: Axis,
    verticalAxis: Axis,
) {

    val background = MaterialTheme.colors.primarySurface
    val labelColor = MaterialTheme.colors.secondary
    val pointColor = Figma.Purple

    val textPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = 40f
        color = labelColor.toArgb()
    }


    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .background(background)
    ) {

        drawIntoCanvas { canvas ->
            val bounds = 10.dp.toPx()

            val canvasWidth = size.width - bounds
            val canvasHeight = size.height - bounds

            //Labels
            val horizontalLabelWidth = canvasWidth / horizontalAxis.labels.size
            val verticalLabelWidth = canvasHeight / verticalAxis.labels.size

            horizontalAxis.labels.forEachIndexed { xIndex, xLabel ->
                if (xIndex != 0 && xIndex % horizontalAxis.skipRate == 0) {
                    val currentX = xIndex * horizontalLabelWidth
                    canvas.nativeCanvas.drawText(xLabel, currentX, canvasHeight, textPaint)
                }
            }

            verticalAxis.labels.forEachIndexed { yIndex, yLabel ->
                if (yIndex != 0 && yIndex % verticalAxis.skipRate == 0) {
                    val currentY = yIndex * verticalLabelWidth
                    canvas.nativeCanvas.drawText(yLabel, bounds, canvasHeight - currentY, textPaint)
                }
            }


            drawPoints(
                points = points.map { entry ->
                    val xOffset = entry.key * horizontalLabelWidth
                    val yOffset = canvasHeight - entry.value * verticalLabelWidth - (bounds / 2)
                    Offset(xOffset, yOffset)
                },
                pointMode = PointMode.Points,
                color = pointColor,
                strokeWidth = 16f,
                cap = StrokeCap.Round
            )

            val trianglePath = Path().apply {
                points.map { entry ->
                    val xOffset = entry.key * horizontalLabelWidth
                    val yOffset = canvasHeight - entry.value * verticalLabelWidth - (bounds / 2)
                    xOffset to yOffset

                }.sortedBy { it.first }.toList().windowed(2).forEachIndexed { _, list ->

                    val start = list[0]
                    val end = list[1]

                    moveTo(start.first, start.second)
                    lineTo(end.first, end.second)
                }
            }

//            for (i in 1 until yData.size) {
//                val prevX = (i - 1) * xStep
//                val prevY = size.height - yData[i - 1] * yStep
//                val x = i * xStep
//                val y = size.height - yData[i] * yStep
//                val cX1 = prevX + xStep / 2
//                val cY1 = prevY
//                val cX2 = x - xStep / 2
//                val cY2 = y
//                path.cubicTo(cX1, cY1, cX2, cY2, x, y)
//            }
//            drawPath(path, Color.Blue, style = Stroke(width = 10f))


            drawPath(
                path = trianglePath, color = pointColor, style = Stroke(width = 4f)
            )

        }
    }

}

@Composable
fun ChartScaleSelector(
    selectedTab: MutableState<Int>,
    options: List<String>,
    onItemSelected: (Int) -> Unit,
) {
    TabRow(modifier = Modifier
        .wrapContentSize()
        .clip(RoundedCornerShape(24.dp))
        .background(MaterialTheme.colors.surface)
        .padding(8.dp)
        .selectableGroup(),
        selectedTabIndex = selectedTab.value,
        backgroundColor = MaterialTheme.colors.surface,
        divider = { TabRowDefaults.Divider(thickness = 0.dp) },
        indicator = { tabPositions ->
            TabIndicator(tabPositions, selectedTab.value, options[selectedTab.value])
        }) {

        options.forEachIndexed { index, title ->
            Tab(title = title, onClick = { onItemSelected(index) })

        }

    }
}

@Composable
private fun TabIndicator(
    tabPositions: List<TabPosition>,
    tabPage: Int,
    title: String,
) {
    val transition = updateTransition(tabPage, label = "Tab indicator")

    val indicatorLeft by transition.animateDp(
        label = "Indicator left",
        transitionSpec = { spring(stiffness = Spring.StiffnessMedium) }) { page ->
        tabPositions[page].left
    }
    val indicatorRight by transition.animateDp(
        label = "Indicator right",
        transitionSpec = { spring(stiffness = Spring.StiffnessMedium) }) { page ->
        tabPositions[page].right
    }
    val color by transition.animateColor(
        label = "Background color"
    ) { page ->
        if (page == tabPage) MaterialTheme.colors.onSecondary else MaterialTheme.colors.surface
    }

    val alpha = transition.animateFloat(label = "text alpha",
        transitionSpec = { spring(stiffness = Spring.StiffnessVeryLow) }) { page ->
        if (page == tabPage) 1f else 0f
    }

    Tab(
        modifier = Modifier
            .alpha(alpha.value)
            .fillMaxSize()
            .wrapContentSize(align = Alignment.CenterStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .clip(RoundedCornerShape(16.dp))
            .fillMaxSize()
            .background(color),
        title = title,
    )
}

@Composable
fun Tab(
    modifier: Modifier = Modifier,
    title: String,
    textSize: Int = MaterialTheme.typography.subtitle1.fontSize.value.toInt(),
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }

    Text(modifier = modifier
        .clickable(
            interactionSource = interactionSource, indication = null
        ) { onClick() }
        .wrapContentSize()
        .padding(horizontal = 24.dp, vertical = 8.dp),
        text = title,
        style = MaterialTheme.typography.subtitle1.copy(fontSize = textSize.sp))
}


