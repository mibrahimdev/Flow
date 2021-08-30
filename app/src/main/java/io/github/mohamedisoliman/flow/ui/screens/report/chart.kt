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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.mohamedisoliman.flow.ui.theme.Figma


@Preview(showBackground = true)
@Composable
fun PreviewLineChart() {
    LineChart()
}

@Preview
@Composable
fun instagramIcon() {
    val instaColors = listOf(Color.Yellow, Color.Red, Color.Magenta)
    Canvas(
        modifier = Modifier
            .size(100.dp)
            .padding(16.dp)
    ) {
        drawRoundRect(
            brush = Brush.linearGradient(colors = instaColors),
            cornerRadius = CornerRadius(60f, 60f),
            style = Stroke(width = 15f, cap = StrokeCap.Round)
        )
        drawCircle(
            brush = Brush.linearGradient(colors = instaColors),
            radius = 45f,
            style = Stroke(width = 15f, cap = StrokeCap.Round)
        )
        drawCircle(
            brush = Brush.linearGradient(colors = instaColors),
            radius = 13f,
            center = Offset(this.size.width * .80f, this.size.height * 0.20f),
        )
    }
}


@Composable
fun LineChart() {

    val linesColor = Figma.Purple
    val pointColor = Figma.Purple

    val horizontalLabels = (0 until 10).map { it.toString() }
    val verticalLabels = (0 until 10).map { it.toString() }

    val points = mapOf(
        Pair(1f, 5f),
        Pair(2f, 4f),
        Pair(3f, 9f),
        Pair(5f, 6f),
        Pair(6.5f, 4f),
        Pair(8f, 9f)
    )

    val textPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = 64f
        color = 0xffb0b3ff.toInt()
    }



    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.surface)
    ) {
        drawIntoCanvas { canvas ->
            val bounds = 16.dp.toPx()

            val canvasWidth = size.width - bounds
            val canvasHeight = size.height - bounds

            //Labels
            val horizontalLabelWidth = canvasWidth / horizontalLabels.size
            val verticalLabelWidth = canvasHeight / verticalLabels.size

            horizontalLabels.forEachIndexed { index, label ->
                if (index != 0) {
                    val currentX = index * horizontalLabelWidth
                    canvas.nativeCanvas.drawText(label, currentX, canvasHeight, textPaint)
                }
            }

            verticalLabels.forEachIndexed { index, label ->
                if (index != 0) {
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
                strokeWidth = 50f,
                cap = StrokeCap.Round
            )


            points.map { entry ->
                val xOffset = entry.key * horizontalLabelWidth
                val yOffset = canvasHeight - entry.value * verticalLabelWidth - (bounds / 2)
                Offset(xOffset, yOffset)
            }

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


