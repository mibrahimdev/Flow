package io.github.mohamedisoliman.flow.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.mohamedisoliman.flow.R
import io.github.mohamedisoliman.flow.ui.theme.CardSurface
import io.github.mohamedisoliman.flow.ui.theme.Figma

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    Home()
}

@Composable
fun Home() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeTopBar(modifier = Modifier.padding(bottom = 24.dp)) {}
        CurrentTaskCard()
        TodayTasks(modifier = Modifier.padding(top = 32.dp)) {}
    }

}

@Composable
private fun HomeTopBar(modifier: Modifier = Modifier, onClick: () -> Unit) {
    SectionHead(modifier = modifier, endItem = {
        IconButton(modifier = Modifier.size(24.dp), onClick = onClick) {
            Icon(
                painter = painterResource(id = R.drawable.more),
                contentDescription = ""
            )
        }
    })
}

@Composable
fun TodayTasks(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .then(modifier)
    ) {
        SectionHead(
            modifier =
            Modifier.padding(bottom = 24.dp),
            title = "Today",
            endItem = {
                Text(modifier = Modifier.clickable(onClick = onClick),
                    text = "See All", style = MaterialTheme.typography.subtitle1)
            }
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(5) {
                TaskCard(task = Task(
                    taskName = "UI Design",
                    taskTags = listOf(TaskTag("Work", Figma.Pink),
                        TaskTag("Rasion Project", Figma.Purple)),
                    taskTimer = "00:42:21"
                ))
            }
        }
    }
}

@Composable
fun TaskCard(task: Task = Task()) {
    CardSurface(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = ""
            )

            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = task.taskName)
                Tags()
            }
        }
    }
}

@Composable
private fun StartTask(task: Task) {
    Column(
        modifier = Modifier.wrapContentHeight(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = task.taskTimer)
        IconButton(onClick = { /*TODO*/ }) {
            Icon(painter = painterResource(id = R.drawable.play), contentDescription = "")
        }
    }
}

@Composable
private fun Tags(tags: List<TaskTag> = emptyList()) {
    Row(modifier = Modifier.wrapContentWidth()) {
        tags.take(2).forEach {
            TagView(it.tagName, it.tagColor)
        }
    }
}

@Composable
fun TagView(tagName: String, tagColor: Color) {
    Surface(color = tagColor.copy(alpha = 20F), shape = RoundedCornerShape(4.dp)) {
        Text(text = tagName, style = MaterialTheme.typography.caption.copy(color = tagColor))
    }
}

@Composable
fun SectionHead(
    modifier: Modifier = Modifier,
    title: String = "Task",
    endItem: @Composable () -> Unit = {},
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = title,
            style = MaterialTheme.typography.h4.copy(fontSize = 24.sp)
        )
        endItem()
    }
}


@Composable
fun CurrentTaskCard(
    modifier: Modifier = Modifier,
    taskTimer: String = "00:32:10",
    project: String = "Rasion Project",
    projectTint: Color = Figma.Purple,
    onClick: () -> Unit = {},
) {
    CardSurface(modifier = Modifier
        .wrapContentHeight()
        .then(modifier)
    ) {
        Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceAround) {
            TaskView(taskTimer, onClick)
            ProjectView(
                projectName = project,
                projectTint = projectTint
            )

        }
    }

}

@Composable
private fun TaskView(taskTimer: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = taskTimer,
            style = MaterialTheme.typography.h4
        )
        IconButton(onClick = onClick) {
            Icon(modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun ProjectView(
    modifier: Modifier = Modifier,
    projectName: String = "",
    projectTint: Color = Color.Transparent,
) {
    Row(modifier = Modifier
        .padding(top = 24.dp)
        .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        Icon(
            modifier = Modifier
                .padding(end = 12.dp)
                .size(12.dp),
            painter = painterResource(R.drawable.eclipse),
            contentDescription = "",
            tint = projectTint
        )
        Text(text = projectName)
    }
}

data class Task(
    val taskName: String = "",
    val taskTimer: String = "",
    val taskTags: List<TaskTag> = emptyList(),
)

data class TaskTag(
    val tagName: String = "",
    val tagColor: Color = Color.Transparent,
)
