package io.github.mohamedisoliman.flow.ui.screens.report

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.mohamedisoliman.flow.R
import io.github.mohamedisoliman.flow.ui.CardSurface

@Preview(showBackground = true)
@Composable
fun PreviewReport() {
    ReportScreen()
}

@Composable
fun ReportScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderLayout()
        SummeryLayout()
        ChartLayout()
    }

}

@Composable
fun ChartLayout() {
    val options = Scale.values().map { stringResource(id = it.stringResId) }
    val selectedTab = remember { mutableStateOf(0) }
    val onItemSelected: (Int) -> Unit = { selectedTab.value = it }
    val value = selectedTab.value
    val horizontalAxis = value.axis()
    val maxTasksCount = value.maxTasksCounter()

    val verticalAxis = if (maxTasksCount > 10) {
        Axis(max = 100, skipRate = 5)
    } else if (maxTasksCount > 100) {
        Axis(max = 100, skipRate = 4)
    } else {
        Axis(max = 50, skipRate = 5)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 42.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ChartScaleSelector(selectedTab, options, onItemSelected)

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )

        val horizontalLabels = (0 until horizontalAxis.max).map { it.toString() }
        val verticalLabels = (0 until verticalAxis.max).map { it.toString() }

        val points = mapOf(
            1f to 5f,
            2f to 4f,
            3f to 2f,
            4f to 7f,
            5f to 2.2f,
            6.3f to 3.5f,
            7.4f to 6f,
            8.5f to 4f,
            8f to 9f,
            9f to 1f
        )

    }
}

@Composable
private fun HeaderLayout() {
    Text(
        text = stringResource(R.string.reportScreenTitle),
        style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Medium)
    )
}

@Composable
private fun SummeryLayout() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 42.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SummeryCard(
            modifier = Modifier.weight(0.1f, fill = false),
            title = buildAnnotatedString { append("Task \nCompleted") },
            text = buildAnnotatedString { append("20") },
            iconId = R.drawable.task_completed
        )


        Spacer(modifier = Modifier.width(16.dp))

        SummeryCard(
            modifier = Modifier.weight(0.1f, fill = false),
            title = buildAnnotatedString { append("Time \nDuration") },
            text = buildAnnotatedString {
                append("1")
                withStyle(
                    style = SpanStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    )
                ) { append("h ") }
                append("44")
                withStyle(
                    style = SpanStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    )
                ) { append("m ") }
            },
            iconId = R.drawable.time_duration
        )
    }
}

@Composable
fun SummeryCard(
    modifier: Modifier = Modifier,
    title: AnnotatedString = buildAnnotatedString { append("") },
    text: AnnotatedString = buildAnnotatedString { append("") },
    @DrawableRes
    iconId: Int = R.drawable.task_completed,
) {
    CardSurface(
        modifier = modifier.height(130.dp) //Todo: should be more dynamic
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    imageVector = ImageVector.vectorResource(id = iconId),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
                Text(text = title, style = MaterialTheme.typography.subtitle1)

            }

            Text(text = text, style = MaterialTheme.typography.h4)
        }
    }

}