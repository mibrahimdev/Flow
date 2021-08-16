package io.github.mohamedisoliman.flow.ui.screens

import android.os.CountDownTimer
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.mohamedisoliman.flow.R
import io.github.mohamedisoliman.flow.testing.tasks
import io.github.mohamedisoliman.flow.ui.*
import io.github.mohamedisoliman.flow.ui.screens.home.Task
import io.github.mohamedisoliman.flow.ui.theme.Figma
import io.github.mohamedisoliman.flow.ui.theme.radial
import kotlin.time.ExperimentalTime


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

    CircularCountDown(
        modifier = modifier
            .padding(24.dp)
            .size(300.dp)
            .padding(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
        progress = 60f,
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