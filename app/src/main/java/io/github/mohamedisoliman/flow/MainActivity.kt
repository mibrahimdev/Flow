package io.github.mohamedisoliman.flow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.mohamedisoliman.flow.ui.AppBottomBar
import io.github.mohamedisoliman.flow.ui.AppNavigation
import io.github.mohamedisoliman.flow.ui.Screen
import io.github.mohamedisoliman.flow.ui.theme.FlowTheme

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val navController = rememberNavController()
            FlowApp(navController = navController)
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun FlowApp(
    navController: NavHostController,
    darkTheme: @Composable () -> Boolean = { isSystemInDarkTheme() }
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    FlowTheme(darkTheme = darkTheme()) {
        Scaffold(
            bottomBar = {
                val route = currentDestination?.route
                val visible = route?.contains(Screen.TaskTimer.route, true) != true
                AppBottomBar(navController = navController, visible = visible)
            },
        ) {
            AppNavigation(navController = navController, modifier = Modifier.padding(it))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlowTheme {
        Greeting("Android")
    }
}