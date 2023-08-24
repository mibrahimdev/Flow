package io.github.mohamedisoliman.flow

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.mohamedisoliman.flow.fake.tasks
import io.github.mohamedisoliman.flow.ui.Screen
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tools.fastlane.screengrab.Screengrab

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {

    }

    private fun setupAppWithTheme(isDark: @Composable () -> Boolean = { false }) {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            FlowApp(navController = navController, darkTheme = isDark)
        }
    }

    @Test
    fun is_home_screen_displayed_at_first() {
        setupAppWithTheme()
        //Home screen
        val screenName = Screen.Home.route

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, screenName)
        Screengrab.screenshot(screenName)
    }

    @Test
    fun is_home_screen_displayed_at_first_dark() {
        setupAppWithTheme(isDark = { true })
        //Home screen
        val screenName = Screen.Home.route

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, screenName)
        Screengrab.screenshot(screenName + "_dark")
    }

    @Test
    fun clicking_item_card_should_display_timer_screen() {
        setupAppWithTheme()
        val taskName = tasks.first()
        val tag = taskName.tags.first().name

        composeTestRule.onNodeWithContentDescription(taskName.name).performClick()
        composeTestRule.onNodeWithContentDescription(tag).assertIsDisplayed()
        Screengrab.screenshot(Screen.TaskTimer.route)
    }

    @Test
    fun clicking_item_card_should_display_timer_screen_dark() {
        setupAppWithTheme(isDark = { true })

        val taskName = tasks.first()
        val tag = taskName.tags.first().name

        composeTestRule.onNodeWithContentDescription(taskName.name).performClick()
        composeTestRule.onNodeWithContentDescription(tag).assertIsDisplayed()
        Screengrab.screenshot(Screen.TaskTimer.route + "_dark")
    }

    @Test
    fun clicking_report_icon_should_navigate_to_report_screen() {
        setupAppWithTheme()
        val screen = Screen.Report.route
        val reportScreenTitle = context.getString(R.string.reportScreenTitle)
        composeTestRule.onNodeWithContentDescription(screen).performClick()
        composeTestRule.onNodeWithContentDescription(reportScreenTitle).assertIsDisplayed()
        Screengrab.screenshot(Screen.Report.route)
    }

    @Test
    fun clicking_report_icon_should_navigate_to_report_screen_dark() {
        setupAppWithTheme(isDark = { true })

        val screen = Screen.Report.route
        val reportScreenTitle = context.getString(R.string.reportScreenTitle)
        composeTestRule.onNodeWithContentDescription(screen).performClick()
        composeTestRule.onNodeWithContentDescription(reportScreenTitle).assertIsDisplayed()
        Screengrab.screenshot(Screen.Report.route + "_dark")
    }

}