package io.github.mohamedisoliman.flow.fake

import androidx.compose.ui.graphics.Color
import io.github.mohamedisoliman.flow.R
import io.github.mohamedisoliman.flow.ui.screens.home.Project
import io.github.mohamedisoliman.flow.ui.screens.home.Task
import io.github.mohamedisoliman.flow.ui.screens.home.TaskTag
import io.github.mohamedisoliman.flow.ui.theme.Figma

private val previewData = listOf(
    Task(
        id = 30,
        name = "100x Sit-Up",
        project = Project("Rasion Project", Figma.Pink, R.drawable.barbell),
        tags = listOf(TaskTag("Work", Color.Yellow), TaskTag("Rasion Project", Figma.Purple)),
        time = "00:42:21"
    ), Task(
        id = 40,
        name = "UI Design",
        project = Project("Rasion Project", Figma.Green, R.drawable.code_slash),
        tags = listOf(TaskTag("Personal", Figma.Black), TaskTag("Rasion Project", Figma.Blue)),
        time = "00:42:21"
    ), Task(
        id = 50,

        name = "100x Sit-Up",
        project = Project("Rasion Project", Figma.Blue, R.drawable.desktop),
        tags = listOf(TaskTag("Work", Figma.Green), TaskTag("Side project", Figma.Orange)),
        time = "00:42:21"
    ), Task(
        id = 60,

        name = "Learn HTML & CSS",
        project = Project("Rasion Project", Figma.Orange, R.drawable.code_slash),
        tags = listOf(TaskTag("Work", Figma.Green), TaskTag("Workout", Figma.Purple)),
        time = "00:42:21"
    ), Task(
        id = 70,

        name = "Read 10 pages of book",
        project = Project("Rasion Project", Figma.Purple, R.drawable.book),
        tags = listOf(TaskTag("Work", Figma.Orange), TaskTag("Rasion Project", Figma.LightGrey)),
        time = "00:42:21"
    ), Task(
        id = 80,

        name = "Read 10 pages of book",
        project = Project("Rasion Project", Color.Red, R.drawable.code_slash),
        tags = listOf(TaskTag("Work", Figma.Green), TaskTag("Workout", Color.Red)),
        time = "00:42:21"
    ), Task(
        id = 90,

        name = "Read 10 pages of book",
        project = Project("Rasion Project", Color.Magenta, R.drawable.desktop),
        tags = listOf(TaskTag("Work", Figma.Pink), TaskTag("Reading 100 Book", Color.Yellow)),
        time = "00:42:21"
    ), Task(
        id = 100,

        name = "Read 10 pages of book",
        project = Project("Rasion Project", Color.Cyan, R.drawable.book),
        tags = listOf(TaskTag("Work", Color.Cyan), TaskTag("Rasion Project", Figma.Orange)),
        time = "00:42:21"
    ), Task(
        id = 110,
        name = "Read 10 pages of book",
        project = Project("Rasion Project", Color.DarkGray, R.drawable.barbell),
        tags = listOf(TaskTag("Work", Color.Yellow), TaskTag("Rasion Project", Figma.Purple)),
        time = "00:42:21"
    )
)

val tasks = (previewData + previewData).shuffled()

val currentTask = tasks.first()