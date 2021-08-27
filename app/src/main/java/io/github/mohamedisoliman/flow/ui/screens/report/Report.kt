package io.github.mohamedisoliman.flow.ui.screens.report

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.mohamedisoliman.flow.R
import io.github.mohamedisoliman.flow.ui.CardSurface
import io.github.mohamedisoliman.flow.ui.theme.Figma

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
    val options = listOf("Day", "Week", "Month")
    val selectedTab = remember { mutableStateOf(0) }
    val onItemSelected: (Int) -> Unit = { selectedTab.value = it }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 42.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChartScaleSelector(selectedTab, options, onItemSelected)
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
            iconId = R.drawable.task_completed)


        Spacer(modifier = Modifier.width(16.dp))

        SummeryCard(
            modifier = Modifier.weight(0.1f, fill = false),
            title = buildAnnotatedString { append("Time \nDuration") },
            text = buildAnnotatedString {
                append("1")
                withStyle(style = SpanStyle(fontSize = 14.sp,
                    fontWeight = FontWeight.Light)) { append("h ") }
                append("44")
                withStyle(style = SpanStyle(fontSize = 14.sp,
                    fontWeight = FontWeight.Light)) { append("m ") }
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
                    painter = painterResource(id = iconId),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
                Text(text = title, style = MaterialTheme.typography.subtitle1)

            }

            Text(text = text, style = MaterialTheme.typography.h4)
        }
    }

}