package io.github.mohamedisoliman.flow.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.mohamedisoliman.flow.R
import io.github.mohamedisoliman.flow.testing.tasks
import io.github.mohamedisoliman.flow.ui.CircularCountDown
import io.github.mohamedisoliman.flow.ui.ProjectView
import io.github.mohamedisoliman.flow.ui.TagStyle
import io.github.mohamedisoliman.flow.ui.TagView
import io.github.mohamedisoliman.flow.ui.screens.home.Task
import io.github.mohamedisoliman.flow.ui.theme.Figma
import io.github.mohamedisoliman.flow.ui.theme.radial
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlin.time.ExperimentalTime


const val interval = 1000

@Preview(showBackground = true)
@Composable
fun PreviewTaskTimer() {
    TaskTimer(taskId = 40)
}

@Composable
fun TaskTimer(taskId: Int?) {
    val task = tasks.firstOrNull { it.id == taskId }
    TimerContainer(task = task)
}

@Composable
fun TimerContainer(modifier: Modifier = Modifier, task: Task?) {
    val taskState = remember { task }
    val tag = taskState?.tags?.firstOrNull()
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tag?.let {
                TagView(
                    tagName = it.name,
                    tagColor = it.color,
                    tagStyle = TagStyle.Bordered
                )
            }

            PinToggle()
        }


        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            if (task != null) {
                Text(text = task.name, style = MaterialTheme.typography.h5)

                ProjectView(
                    projectName = task.project.name,
                    projectTint = task.project.color
                )
            }

            CountDownCircle()
        }


    }


}

@OptIn(ExperimentalTime::class, kotlinx.coroutines.ObsoleteCoroutinesApi::class)
@Composable
fun CountDownCircle(modifier: Modifier = Modifier) {

    val target = 60 //seconds
    val lastPause = (20.toFloat() / target)
    val progress = continuousAnimation(lastPause, target) //start
    CircularCountDown(
        modifier = modifier
            .padding(24.dp)
            .size(300.dp)
            .padding(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
        progress = progress.value,
        color = Figma.Purple.radial(),
        strokeWidth = 20.dp
    )
}

@Composable
private fun continuousAnimation(
    lastPause: Float,
    target: Int,
): Animatable<Float, AnimationVector1D> {
    //    val progress = progressAnimation(target)
//    val progress = progressTransitionAnimation(target)
    //to create continuous animation we could set the target to full arc [360f]
    // and animation time would be the time target.

    val progress = remember { Animatable(lastPause * 360f) } //start
    LaunchedEffect(key1 = progress) {
        progress.animateTo(targetValue = 360f,
            animationSpec = tween(target * interval, easing = LinearEasing))
    }
    return progress
}

@Composable
private fun progressTransitionAnimation(target: Int): State<Float> {
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
private fun progressAnimation(seconds: Int): Animatable<Float, AnimationVector1D> {
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
private fun PinToggle() {
    IconToggleButton(
        checked = false, onCheckedChange = {

        }) {
        Icon(
            painter = painterResource(id = R.drawable.pin_outlined),
            contentDescription = ""
        )
    }
}