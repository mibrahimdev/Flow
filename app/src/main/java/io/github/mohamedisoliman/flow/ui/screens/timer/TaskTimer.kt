package io.github.mohamedisoliman.flow.ui.screens.timer

import android.text.format.DateUtils
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.mohamedisoliman.flow.R
import io.github.mohamedisoliman.flow.fake.tasks
import io.github.mohamedisoliman.flow.ui.*
import io.github.mohamedisoliman.flow.ui.screens.home.Task
import io.github.mohamedisoliman.flow.ui.screens.home.TaskTag
import io.github.mohamedisoliman.flow.ui.theme.Figma
import kotlinx.coroutines.ObsoleteCoroutinesApi


const val interval = 1000

@OptIn(ObsoleteCoroutinesApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewTaskTimer() {
    TaskTimer(taskId = 40)
}

@ObsoleteCoroutinesApi
@Composable
fun TaskTimer(taskId: Int?) {
    val task = tasks.firstOrNull { it.id == taskId }
    TimerContainer(task = task)
}

@ObsoleteCoroutinesApi
@Composable
fun TimerContainer(modifier: Modifier = Modifier, task: Task?) {
    val taskState = remember { task }
    val tag = taskState?.tags?.firstOrNull()

    val targetTime = 60L * 3
    val lastPause = 60L * 2
    val tick = remember { mutableStateOf(lastPause) }

    startTimer(targetTime, lastPause) {
        tick.value = it
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {

        TimerHeader(tag)

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

            TimerCountdownContainer(modifier, targetTime, lastPause, tick)
        }

        Row(
            modifier = modifier
                .padding(start = 32.dp, end = 32.dp, top = 100.dp, bottom = 32.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleButton(id = R.drawable.pause_filled) {

            }
            CircleButton(id = R.drawable.stop_filled) {

            }
        }


    }


}

@Composable
private fun TimerCountdownContainer(
    modifier: Modifier,
    targetTime: Long,
    lastPause: Long,
    tick: MutableState<Long>,
) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {

        CountDownCircle(
            modifier = modifier
                .padding(24.dp)
                .size(300.dp)
                .padding(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.Center),
            targetTimeInSeconds = targetTime,
            currentTimeInSeconds = lastPause
        )
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = tick.value.formatDuration(),
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Medium)
        )

    }
}

@Composable
private fun TimerHeader(tag: TaskTag?) {

    val pinned = remember { mutableStateOf(false) }


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

        PinToggle(checked = pinned.value) {
            pinned.value = it
        }
    }
}

@Composable
private fun CircleButton(
    modifier: Modifier = Modifier,
    @DrawableRes id: Int,
    onClick: () -> Unit = {},
) {

    IconButton(
        modifier = modifier
            .clip(CircleShape)
            .background(color = MaterialTheme.colors.surface)
            .size(60.dp),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = id),
            contentDescription = ""
        )
    }
}

fun Long.formatDuration(): String = if (this < 60) {
    toString()
} else {
    DateUtils.formatElapsedTime(this)
}

@Composable
fun CountDownCircle(
    modifier: Modifier = Modifier,
    targetTimeInSeconds: Long = 0,
    currentTimeInSeconds: Long = 0,
) {

    val progress = continuousAnimation(currentTimeInSeconds, targetTimeInSeconds) //start
    CircularCountDown(
        modifier = modifier,
        progress = progress.value,
        color = Figma.Purple,
        strokeWidth = 20.dp
    )
}

@Composable
private fun PinToggle(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onChecked: (Boolean) -> Unit = {},
) {
    IconToggleButton(
        checked = checked, onCheckedChange = onChecked) {
        Icon(
            painter = painterResource(id = if (checked) R.drawable.pin_filled else R.drawable.pin_outlined),
            contentDescription = ""
        )
    }
}