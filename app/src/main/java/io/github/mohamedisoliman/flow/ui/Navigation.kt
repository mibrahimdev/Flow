package io.github.mohamedisoliman.flow.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navArgument
import io.github.mohamedisoliman.flow.R
import io.github.mohamedisoliman.flow.testing.currentTask
import io.github.mohamedisoliman.flow.testing.tasks
import io.github.mohamedisoliman.flow.ui.screens.CurrentTaskScreen
import io.github.mohamedisoliman.flow.ui.screens.home.HomeScreen
import io.github.mohamedisoliman.flow.ui.screens.report.ReportScreen


@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(currentTask = currentTask, data = tasks, onTaskClicked = {
                navController.navigate("${Screen.CurrentTask.route}/${it.id}")
            })
        }
        composable(Screen.Report.route) {
            ReportScreen()
        }

        composable(
            route = "${Screen.CurrentTask.route}/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { entry ->
            val taskId = entry.arguments?.getInt("taskId")
            CurrentTaskScreen(taskId)
        }
    }


}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Home : Screen("home", R.string.home)
    object Report : Screen("report", R.string.report)
    object CurrentTask : Screen("Current_task", R.string.current_task)
}

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    BottomNavigation(
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(start = 16.dp, end = 16.dp)
//            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination


        NavigationItem(currentDestination, Screen.Home, navController) {
            Icon(
                painter = painterResource(R.drawable.time_filled),
                contentDescription = "",
//                tint = MaterialTheme.colors.onSurface
            )
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { /*TODO*/ }
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "")
        }

        NavigationItem(currentDestination, Screen.Report, navController) {
            Icon(
                painter = painterResource(R.drawable.pie_char_filled),
                contentDescription = "",
//                tint = MaterialTheme.colors.onSurface
            )
        }
    }

}

@Composable
private fun RowScope.NavigationItem(
    currentDestination: NavDestination?,
    screen: Screen,
    navController: NavHostController,
    icon: @Composable () -> Unit = {},
) {
    BottomNavigationItem(icon = icon,
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
            navController.navigate(screen.route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }

    )
}

