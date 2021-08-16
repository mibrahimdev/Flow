package io.github.mohamedisoliman.flow.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun PreviewCircularCountDown() {

    CircularProgressIndicator()

    CircularCountDown(
        modifier = Modifier
            .padding(24.dp)
            .size(300.dp),
        progress = 0.5f,
        strokeWidth = 20.dp
    )
}

@Composable
fun CircularCountDown(
    modifier: Modifier = Modifier,
    progress: Float = 0.001f,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth,
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
    }

    val backgroundArkColor = MaterialTheme.colors.surface

    Canvas(
        modifier
            .progressSemantics(progress)
            .size(40.dp)
            .focusable()
    ) {
        // Start at 12 O'clock
        val startAngle = 270f
        val sweep = progress /** 360f*/
        // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
        // To do this we need to remove half the stroke width from the total diameter for both sides.
        val diameterOffset = stroke.width / 2
        val arcDimen = size.width - 2 * diameterOffset

        //background white arc
        drawBackgroundArc(backgroundArkColor, startAngle, diameterOffset, arcDimen, strokeWidth)

        //the progress arc
        drawProgressArc(color, startAngle, sweep, diameterOffset, arcDimen, stroke)
    }
}

private fun DrawScope.drawBackgroundArc(
    backgroundArkColor: Color,
    startAngle: Float,
    diameterOffset: Float,
    arcDimen: Float,
    strokeWidth: Dp,
) {
    drawArc(
        color = backgroundArkColor,
        startAngle = startAngle,
        sweepAngle = 360f,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawProgressArc(
    color: Color,
    startAngle: Float,
    sweep: Float,
    diameterOffset: Float,
    arcDimen: Float,
    stroke: Stroke,
) {
    drawArc(
        brush = Brush.radialGradient(
            listOf(Color.White, color),
            tileMode = TileMode.Mirror
        ),
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}