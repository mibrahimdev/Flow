package io.github.mohamedisoliman.flow.ui

import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import io.github.mohamedisoliman.flow.R
import io.github.mohamedisoliman.flow.fake.currentTask
import io.github.mohamedisoliman.flow.fake.tasks
import io.github.mohamedisoliman.flow.ui.screens.timer.TaskTimer
import io.github.mohamedisoliman.flow.ui.screens.home.HomeScreen
import io.github.mohamedisoliman.flow.ui.screens.report.ReportScreen
import kotlinx.coroutines.ObsoleteCoroutinesApi


@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Report.route,//TODO: testing
        modifier = modifier
    ) {
        homeComposable(navController)

        report(navController)

        taskTimer(navController)
    }


}

private fun @Composable NavGraphBuilder.report(navController: NavHostController) {
    composable(Screen.Report.route) {
        ReportScreen()
    }
}


@OptIn(ObsoleteCoroutinesApi::class)
private fun @Composable NavGraphBuilder.taskTimer(navController: NavHostController) {
    composable(
        route = "${Screen.TaskTimer.route}/{taskId}",
        arguments = listOf(navArgument("taskId") { type = NavType.IntType })
    ) { entry ->
        val taskId = entry.arguments?.getInt("taskId")
        TaskTimer(taskId)
    }
}

private fun @Composable NavGraphBuilder.homeComposable(navController: NavHostController) {
    composable(Screen.Home.route) {
        HomeScreen(currentTask = currentTask, data = tasks, onTaskClicked = {
            navController.navigate("${Screen.TaskTimer.route}/${it.id}")
        })
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Home : Screen("home", R.string.home)
    object Report : Screen("report", R.string.report)
    object TaskTimer : Screen("Current_task", R.string.current_task)
}

@ExperimentalAnimationApi
@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    visible: Boolean = true,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
        )

    ) {
        BottomNavigation(
            modifier = modifier
                .fillMaxWidth()
                .height(85.dp)
                .padding(start = 16.dp, end = 16.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colors.primarySurface,
        ) {


            NavigationItem(currentDestination,
                Screen.Home,
                navController,
                icon = { modifier, tint ->
                    Icon(
                        modifier = modifier,
                        tint = tint,
                        painter = painterResource(R.drawable.time_filled),
                        contentDescription = "",
                    )
                })

            FloatingActionButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { /*TODO*/ }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "")
            }

            NavigationItem(currentDestination, Screen.Report, navController,
                icon = { modifier, tint ->
                    Icon(
                        modifier = modifier,
                        tint = tint,
                        painter = painterResource(R.drawable.pie_char_filled),
                        contentDescription = "",
                    )
                })
        }

    }


}

@Composable
fun AnimatableIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    iconSize: Dp = 48.dp,
    scale: Float = 1f,
    color: Color = Color.Transparent,
    onClick: () -> Unit,
) {
    // Animation params
    val animatedScale: Float by animateFloatAsState(
        targetValue = scale,
        // Here the animation spec serves no purpose but to demonstrate in slow speed.
        animationSpec = TweenSpec(
            durationMillis = 2000,
            easing = FastOutSlowInEasing
        )
    )
    val animatedColor by animateColorAsState(
        targetValue = color,
        animationSpec = TweenSpec(
            durationMillis = 2000,
            easing = FastOutSlowInEasing
        )
    )

    IconButton(
        onClick = onClick,
        modifier = modifier.size(iconSize)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "dummy",
            tint = animatedColor,
            modifier = modifier.scale(animatedScale)
        )
    }
}


@Composable
private fun RowScope.NavigationItem(
    currentDestination: NavDestination?,
    screen: Screen,
    navController: NavHostController,
    icon: @Composable (modifier: Modifier, tint: Color) -> Unit = { _, _ -> },
) {

    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val targetScale = if (selected) 1.1f else 1f
    val targetColor = if (selected) MaterialTheme.colors.onSurface else Color.LightGray


    // Animation params
    val animatedScale: Float by animateFloatAsState(
        targetValue = targetScale,
        // Here the animation spec serves no purpose but to demonstrate in slow speed.
        animationSpec = TweenSpec(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )
    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = TweenSpec(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )

    BottomNavigationItem(icon = {
        icon(modifier = Modifier.scale(animatedScale), tint = animatedColor)
    },
        selected = selected,
        onClick = {
            navController.navigate(screen.route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

    )
}

