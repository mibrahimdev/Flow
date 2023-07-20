package io.github.mohamedisoliman.flow.ui.screens.report

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Axis(
    val max: Int = 1,
    val skipRate: Int = 1,
    val labels: List<String> = emptyList(),
)

@Preview()
@Composable
fun PreviewLineChart() {

    val points = mapOf(
        10f to 60f,
        11f to 15f,
        12f to 15f,
        16f to 2.5f,
        18f to 2f,
    )

    CustomBezierChart(
        data = points, modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun ChartScaleSelector(
    selectedTab: MutableState<Int>,
    options: List<String>,
    onItemSelected: (Int) -> Unit,
) {
    TabRow(modifier = Modifier
        .wrapContentSize()
        .clip(RoundedCornerShape(24.dp))
        .background(MaterialTheme.colors.surface)
        .padding(8.dp)
        .selectableGroup(),
        selectedTabIndex = selectedTab.value,
        backgroundColor = MaterialTheme.colors.surface,
        divider = { TabRowDefaults.Divider(thickness = 0.dp) },
        indicator = { tabPositions ->
            TabIndicator(tabPositions, selectedTab.value, options[selectedTab.value])
        }) {

        options.forEachIndexed { index, title ->
            Tab(title = title, onClick = { onItemSelected(index) })

        }

    }
}

@Composable
private fun TabIndicator(
    tabPositions: List<TabPosition>,
    tabPage: Int,
    title: String,
) {
    val transition = updateTransition(tabPage, label = "Tab indicator")

    val indicatorLeft by transition.animateDp(label = "Indicator left",
        transitionSpec = { spring(stiffness = Spring.StiffnessMedium) }) { page ->
        tabPositions[page].left
    }
    val indicatorRight by transition.animateDp(label = "Indicator right",
        transitionSpec = { spring(stiffness = Spring.StiffnessMedium) }) { page ->
        tabPositions[page].right
    }
    val color by transition.animateColor(
        label = "Background color"
    ) { page ->
        if (page == tabPage) MaterialTheme.colors.onSecondary else MaterialTheme.colors.surface
    }

    val alpha = transition.animateFloat(
        label = "text alpha",
        transitionSpec = { spring(stiffness = Spring.StiffnessVeryLow) }) { page ->
        if (page == tabPage) 1f else 0f
    }

    Tab(
        modifier = Modifier
            .alpha(alpha.value)
            .fillMaxSize()
            .wrapContentSize(align = Alignment.CenterStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .clip(RoundedCornerShape(16.dp))
            .fillMaxSize()
            .background(color),
        title = title,
    )
}

@Composable
fun Tab(
    modifier: Modifier = Modifier,
    title: String,
    textSize: Int = MaterialTheme.typography.subtitle1.fontSize.value.toInt(),
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }

    Text(modifier = modifier
        .clickable(
            interactionSource = interactionSource, indication = null
        ) { onClick() }
        .wrapContentSize()
        .padding(horizontal = 24.dp, vertical = 8.dp),
        text = title,
        style = MaterialTheme.typography.subtitle1.copy(fontSize = textSize.sp))
}


