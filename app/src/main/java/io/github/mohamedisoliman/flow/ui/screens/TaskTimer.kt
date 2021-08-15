package io.github.mohamedisoliman.flow.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.mohamedisoliman.flow.R
import io.github.mohamedisoliman.flow.testing.tasks
import io.github.mohamedisoliman.flow.ui.CardSurface
import io.github.mohamedisoliman.flow.ui.TagStyle
import io.github.mohamedisoliman.flow.ui.TagView
import io.github.mohamedisoliman.flow.ui.screens.home.Task


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
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        tag?.let {
            TagView(
                modifier = Modifier.padding(16.dp),
                tagName = it.name,
                tagColor = it.color,
                tagStyle = TagStyle.Bordered
            )
        }

        IconToggleButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            checked = false, onCheckedChange = {

            }) {
            Icon(
                painter = painterResource(id = R.drawable.pin_outlined),
                contentDescription = ""
            )
        }


    }


}
