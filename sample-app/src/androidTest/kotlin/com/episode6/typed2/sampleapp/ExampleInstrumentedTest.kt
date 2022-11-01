package com.episode6.typed2.sampleapp

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
class ExampleInstrumentedTest {

  @get:Rule val hiltRule = HiltAndroidRule(this)
  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun useAppContext() {
    // Context of the app under test.
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    assertEquals("com.episode6.typed2.sampleapp", appContext.packageName)


    composeTestRule.onNodeWithTag("TEST_TAG")
      .assertExists()
      .assertTextContains("Hello there")
  }
}
