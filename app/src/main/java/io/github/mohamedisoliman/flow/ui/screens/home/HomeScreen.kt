package io.github.mohamedisoliman.flow.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.Navigator
import io.github.mohamedisoliman.flow.R
import io.github.mohamedisoliman.flow.testing.currentTask
import io.github.mohamedisoliman.flow.testing.tasks
import io.github.mohamedisoliman.flow.ui.CardSurface

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(currentTask = currentTask, data = tasks)
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    data: List<Task> = emptyList(),
    currentTask: Task = Task(),
    onTaskClicked: (Task) -> Unit = {},
) {
    val tasks = remember { data }
    val currentTaskState = remember { currentTask }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(all = 16.dp)
    ) {
        items(1) {
            HomeTopBar(modifier = Modifier.padding(bottom = 16.dp)) {}
            CurrentTaskCard(task = currentTask, onClick = { onTaskClicked(currentTaskState) })
        }
        item {
            SectionHead(
                title = stringResource(R.string.today),
                endItem = {
                    TextButton(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.onSurface)
                    ) {
                        Text(
                            text = stringResource(R.string.see_all),
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                }
            )
        }

        tasks.take(4).forEach {
            item {
                TaskCard(task = it, onTaskClicked = onTaskClicked)
            }
        }
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
fun TaskCard(
    modifier: Modifier = Modifier,
    task: Task = Task(),
    onTaskClicked: (Task) -> Unit,
) {
    CardSurface(
        modifier = modifier
            .height(90.dp)
            .fillMaxWidth()
            .clickable { onTaskClicked(task) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                ProjectImage(modifier = Modifier.padding(end = 16.dp), task)

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = task.name, style = MaterialTheme.typography.subtitle2)
                    TagsRow(tags = task.tags)
                }
            }

            StartTask(task = task)
        }
    }
}

@Composable
private fun ProjectImage(modifier: Modifier = Modifier, task: Task) {
    Image(
        painter = painterResource(task.project.icon),
        contentDescription = "avatar",
        contentScale = ContentScale.Inside,
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color = task.project.color)
    )
}

@Composable
private fun StartTask(
    modifier: Modifier = Modifier,
    task: Task,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier.wrapContentHeight(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = task.time)
        IconButton(onClick = onClick) {
            Icon(painter = painterResource(id = R.drawable.play), contentDescription = "")
        }
    }
}

@Composable
private fun TagsRow(
    modifier: Modifier = Modifier,
    tags: List<TaskTag> = emptyList(),
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            item {
                TagView(tagName = tag.name, tagColor = tag.color)
            }
        }
    }
}

@Composable
fun TagView(tagName: String, tagColor: Color) {
    Surface(color = tagColor.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)) {
        Text(modifier = Modifier.padding(8.dp),
            text = tagName,
            style = MaterialTheme.typography.caption.copy(color = tagColor, fontSize = 12.sp)
        )
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
    task: Task = Task(),
    onClick: (Task) -> Unit = {},
) {
    val taskSate = remember { task }

    CardSurface(modifier = Modifier
        .wrapContentHeight()
        .then(modifier)
        .clickable { onClick(taskSate) }
    ) {
        Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceAround) {
            TaskView(task.time) {

            }
            ProjectView(
                projectName = task.project.name,
                projectTint = task.project.color
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
    val name: String = "",
    val time: String = "",
    val project: Project = Project(),
    val tags: List<TaskTag> = emptyList(),
    val id: Int = -1,
) : Navigator.Extras

data class TaskTag(
    val name: String = "",
    val color: Color = Color.Transparent,
)

data class Project(
    val name: String = "",
    val color: Color = Color.Transparent,
    val icon: Int = R.drawable.desktop,
) {
}
