package io.github.mohamedisoliman.flow

import android.content.Context
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
import tools.fastlane.screengrab.locale.LocaleTestRule

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Rule
    @JvmField
    val localeTestRule = LocaleTestRule()

    private lateinit var navController: TestNavHostController

    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext


    @Before
    fun setup() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            FlowApp(navController = navController, darkTheme = { true })
        }
    }

    @Test
    fun is_home_screen_displayed_at_first() {
        //Home screen
        val screenName = Screen.Home.route

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, screenName)
        Screengrab.screenshot(screenName)

    }

    @Test
    fun clicking_item_card_should_display_timer_screen() {
        val taskName = tasks.first()
        val tag = taskName.tags.first().name

        composeTestRule.onNodeWithContentDescription(taskName.name).performClick()
        composeTestRule.onNodeWithContentDescription(tag).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(tag).assertIsDisplayed()
        Screengrab.screenshot("Timer screen for $taskName")
    }


    @Test
    fun clicking_report_icon_should_navigate_to_report_screen() {
        val screen = Screen.Report.route
        val reportScreenTitle = context.getString(R.string.reportScreenTitle)
        composeTestRule.onNodeWithContentDescription(screen).performClick()
        composeTestRule.onNodeWithContentDescription(reportScreenTitle).assertIsDisplayed()
        Screengrab.screenshot("Report Screen")
    }
}