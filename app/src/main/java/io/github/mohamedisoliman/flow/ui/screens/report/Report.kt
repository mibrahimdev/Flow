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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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

        val data = mapOf(
            Pair(13f, 2f),
            Pair(14f, 4f),
            Pair(15f, 2.4f),
            Pair(16f, 1f),
            Pair(19f, 2f),
            Pair(20f, 0.25f)
        )

        BezierChart(
            data = data, modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

    }
}

@Composable
private fun HeaderLayout() {
    val title = stringResource(R.string.reportScreenTitle)

    Text(
        modifier = Modifier.semantics {
            contentDescription = title
        }, text = title, style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Medium)
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
                        fontSize = 14.sp, fontWeight = FontWeight.Light
                    )
                ) { append("h ") }
                append("44")
                withStyle(
                    style = SpanStyle(
                        fontSize = 14.sp, fontWeight = FontWeight.Light
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