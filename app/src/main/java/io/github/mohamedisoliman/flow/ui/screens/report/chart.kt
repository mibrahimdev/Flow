package io.github.mohamedisoliman.flow.ui.screens.report

import android.graphics.Paint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
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
        Pair(1f, 5f),
        Pair(2f, 4f),
        Pair(3f, 2f),
        Pair(4f, 7f),
        Pair(5f, 2.2f),
        Pair(6.3f, 3.5f),
        Pair(7.4f, 6f),
        Pair(8.5f, 4f),
        Pair(8f, 9f),
        Pair(9f, 1f)
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
        textSize = 16f
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

            horizontalAxis.labels.forEachIndexed { index, label ->
                if (index != 0 && index % horizontalAxis.skipRate == 0) {
                    val currentX = index * horizontalLabelWidth
                    canvas.nativeCanvas.drawText(label, currentX, canvasHeight, textPaint)
                }
            }

            verticalAxis.labels.forEachIndexed { index, label ->
                if (index != 0 && index % verticalAxis.skipRate == 0) {
                    val currentY = index * verticalLabelWidth
                    canvas.nativeCanvas.drawText(label, bounds, canvasHeight - currentY, textPaint)
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


            drawPath(
                path = trianglePath,
                color = pointColor,
                style = Stroke(width = 4f)
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
    TabRow(
        modifier = Modifier
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
        }
    ) {

        options.forEachIndexed { index, title ->
            Tab(
                title = title,
                onClick = { onItemSelected(index) }
            )

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
        transitionSpec = { spring(stiffness = Spring.StiffnessMedium) }
    ) { page ->
        tabPositions[page].left
    }
    val indicatorRight by transition.animateDp(
        label = "Indicator right",
        transitionSpec = { spring(stiffness = Spring.StiffnessMedium) }
    ) { page ->
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

    Text(
        modifier = modifier
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .wrapContentSize()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        text = title,
        style = MaterialTheme.typography.subtitle1.copy(fontSize = textSize.sp)
    )
}


