package io.github.mohamedisoliman.flow.ui.screens.timer

import android.text.format.DateUtils
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.mohamedisoliman.flow.R
import io.github.mohamedisoliman.flow.testing.tasks
import io.github.mohamedisoliman.flow.ui.*
import io.github.mohamedisoliman.flow.ui.screens.home.Task
import io.github.mohamedisoliman.flow.ui.theme.Figma
import io.github.mohamedisoliman.flow.ui.theme.radial
import kotlinx.coroutines.ObsoleteCoroutinesApi


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

@ObsoleteCoroutinesApi
@Composable
fun TimerContainer(modifier: Modifier = Modifier, task: Task?) {
    val taskState = remember { task }
    val tag = taskState?.tags?.firstOrNull()

    val targetTime = 60L * 10
    val lastPause = 60L * 7
    val tick = remember { mutableStateOf(lastPause) }

    startTimer(targetTime, lastPause) {
        tick.value = it
    }

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
        color = Figma.Purple.radial(),
        strokeWidth = 20.dp
    )
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