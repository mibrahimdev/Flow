package io.github.mohamedisoliman.flow.ui.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import io.github.mohamedisoliman.flow.testing.tasks


@Composable
fun TaskTimer(taskId: Int?) {
    val task = tasks.firstOrNull { it.id == taskId }
    Text(text = task?.name ?: "Current Task")
}
