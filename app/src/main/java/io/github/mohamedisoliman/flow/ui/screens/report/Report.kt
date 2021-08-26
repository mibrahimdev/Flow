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

        TabRow(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(16.dp))
                .background(Figma.Background)
                .padding(8.dp)
                .selectableGroup(),
            selectedTabIndex = selectedTab.value,
            backgroundColor = Figma.Background,
            indicator = { tabPositions ->
                TabIndicator(tabPositions, selectedTab.value, options[selectedTab.value])
            }
        ) {

            options.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    title = title,
                    onClick = { onItemSelected(index) }
                )

            }


        }


//        ScaleSelector(
//            options = options,
//            selectedTab = selectedTab,
//            onItemSelected = onItemSelected
//        )

    }
}

@Composable
private fun TabIndicator(
    tabPositions: List<TabPosition>,
    tabPage: Int,
    title: String,
) {
    val transition = updateTransition(tabPage, label = "Tab indicator")

    val indicatorLeft by transition.animateDp(
        label = "Indicator left",
        transitionSpec = { spring(stiffness = Spring.StiffnessMedium) }
    ) { page ->
        tabPositions[page].left
    }
    val indicatorRight by transition.animateDp(
        label = "Indicator right",
        transitionSpec = { spring(stiffness = Spring.StiffnessMedium) }
    ) { page ->
        tabPositions[page].right
    }
    val color by transition.animateColor(
        label = "Background color"
    ) { page ->
        if (page == tabPage) MaterialTheme.colors.primaryVariant else Figma.Background
    }

    val alpha = transition.animateFloat(label = "text alpha",
        transitionSpec = { spring(stiffness = Spring.StiffnessVeryLow) }) { page ->
        if (page == tabPage) 1f else 0f
    }


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.CenterStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .clip(RoundedCornerShape(16.dp))
            .fillMaxSize()
            .background(color),
        color = color, shape = RoundedCornerShape(100.dp)) {
        Tab(
            modifier = Modifier.alpha(alpha.value),
            title = title,
        )
    }
}


@Composable
fun ScaleSelector(
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedTab: MutableState<Int>,
    onItemSelected: (Int) -> Unit,
) {

    Row(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(16.dp))
            .background(Figma.Background)
            .padding(8.dp)
            .selectableGroup(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        options.forEachIndexed { index, title ->
            val backgroundColor1 = MaterialTheme.colors.contentColorFor(backgroundColor)
            val selected = selectedTab.value == index

            val interactionSource = MutableInteractionSource()

            ScaleTab(
                modifier = Modifier.selectable(
                    selected = selected,
                    role = Role.Tab,
                    indication = LocalIndication.current,
                    interactionSource = interactionSource
                ) {
                    onItemSelected(index)
                },
                selected = selected,
                text = title,
                backgroundColor = backgroundColor1,
                textStyle = MaterialTheme.typography.subtitle1
            )
        }

    }

}


@Composable
fun Tab(
    modifier: Modifier = Modifier,
    title: String,
    textSize: Int = MaterialTheme.typography.subtitle1.fontSize.value.toInt(),
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }

    Text(
        modifier = modifier
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .wrapContentSize()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        text = title,
        style = MaterialTheme.typography.subtitle1.copy(fontSize = textSize.sp)
    )
}


@Composable
fun ScaleTab(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    text: String = "",
    backgroundColor: Color = MaterialTheme.colors.background,
    textStyle: TextStyle = TextStyle.Default,
) {
    val color by animateColorAsState(targetValue = if (!selected) backgroundColor else MaterialTheme.colors.primaryVariant)
    val style = if (selected)
        textStyle.copy(color = MaterialTheme.colors.contentColorFor(color)) else textStyle
//    ScaleText(modifier = modifier, backgroundColor = color, text = text, style = style)

    if (selected) {
        Surface(color = color, shape = RoundedCornerShape(8.dp)) {
            ScaleText(modifier, backgroundColor, text, style)
        }
    } else {
        ScaleText(modifier, backgroundColor, text, style)
    }
}

@Composable
private fun ScaleText(
    modifier: Modifier,
    backgroundColor: Color,
    text: String,
    style: TextStyle,
) {
    Text(
        modifier = modifier
            .background(backgroundColor)
            .wrapContentSize()
            .padding(horizontal = 32.dp, vertical = 8.dp),
        text = text,
        style = style
    )
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

        modifier = modifier
            .height(130.dp) //Todo: should be more dynamic
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