package io.github.mohamedisoliman.flow.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.*
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
import io.github.mohamedisoliman.flow.ui.screens.interval
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun PreviewCircularCountDown() {

    CircularCountDown(
        modifier = Modifier
            .padding(24.dp)
            .size(300.dp),
        progress = 0.5f * 360f,
        strokeWidth = 20.dp
    )
}

@Composable
fun CircularCountDown(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
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
        val sweep = progress
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


@Composable
fun continuousAnimation(
    lastPause: Long,
    target: Long,
): Animatable<Float, AnimationVector1D> {
    val initialValue = (lastPause.toFloat() / target) * 360f
    val progress = remember { Animatable(initialValue) } //start
    LaunchedEffect(key1 = progress) {
        progress.animateTo(
            targetValue = 360f,
            animationSpec = tween(((target - lastPause) * interval).toInt(), easing = LinearEasing)
        )
    }
    return progress
}

@Composable
fun progressTransitionAnimation(target: Int): State<Float> {
    val currentSeconds = remember { mutableStateOf(0f) }
    val transition = updateTransition(targetState = currentSeconds.value, label = "")
    val progress = transition.animateFloat(
        transitionSpec = { tween(200, easing = FastOutSlowInEasing) }, label = ""
    ) { timeLeft ->
        if (timeLeft < 0) {
            360f
        } else {
            (timeLeft / target) * 360f
        }
    }

    LaunchedEffect(key1 = currentSeconds, block = {
        (0..target).asFlow().onEach { delay(interval.toLong()) }.collect {
            currentSeconds.value = it.toFloat()
        }
    })
    return progress
}

@Composable
fun progressAnimation(seconds: Int): Animatable<Float, AnimationVector1D> {
    val interval = 1000L
    val animateFloat = remember { Animatable(0f) }
    LaunchedEffect(key1 = animateFloat) {
        (0..seconds).asFlow()
            .onEach { delay(interval) }
            .collect {
                animateFloat.animateTo(
                    targetValue = (it.toFloat() / seconds) * 360f,
                    animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                )
            }
    }
    return animateFloat
}

@Composable
fun startTimer(
    targetTime: Long,
    lastPause: Long,
    onTick: (Long) -> Unit,
) {
    LaunchedEffect(key1 = "Timer") {
        (lastPause..targetTime).asFlow().onEach { delay(interval.toLong()) }.collect {
            onTick(it)
        }
    }
}

