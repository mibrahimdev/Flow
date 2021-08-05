package io.github.mohamedisoliman.flow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import io.github.mohamedisoliman.flow.ui.AppBottomBar
import io.github.mohamedisoliman.flow.ui.AppNavigation
import io.github.mohamedisoliman.flow.ui.theme.FlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            val navController = rememberNavController()

            FlowTheme {
                Scaffold(
                    bottomBar = {
                        AppBottomBar(navController = navController)
                    },
                ) {
                    AppNavigation(navController = navController, modifier = Modifier.padding(it))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlowTheme {
        Greeting("Android")
    }
}