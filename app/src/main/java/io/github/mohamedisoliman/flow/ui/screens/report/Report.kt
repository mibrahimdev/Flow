package io.github.mohamedisoliman.flow.ui.screens.report

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.reportScreenTitle),
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Medium)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 42.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SummeryCard(
                title = buildAnnotatedString { append("Task \nCompleted") },
                text = buildAnnotatedString { append("20") },
                iconId = R.drawable.task_completed)

            SummeryCard(
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

}

@Composable
fun SummeryCard(
    modifier: Modifier = Modifier,
    title: AnnotatedString = buildAnnotatedString { append("") },
    text: AnnotatedString = buildAnnotatedString { append("") },
    @DrawableRes
    iconId: Int = R.drawable.task_completed,
) {
    CardSurface(modifier = Modifier
        .height(130.dp) //Todo: should be more dynamic
        .width(160.dp)
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