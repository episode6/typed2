package com.episode6.typed2.sampleapp.nav

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.episode6.typed2.navigation.compose.NavScreen
import kotlinx.coroutines.CoroutineScope

class AppNavigators(
  val navController: NavController,
  val scope: CoroutineScope,
  val context: Context,
)

typealias GoUpNavigator = () -> Unit

fun AppNavigators.goUp(): GoUpNavigator {
  return { navController.navigateUp() }
}

fun ScreenRegistration(screen: NavScreen, content: @Composable ScreenRegistrationContext.() -> Unit) = object : ScreenRegistration {
  override val screen: NavScreen = screen
  @Composable override fun content(backStackEntry: NavBackStackEntry, appNavigators: AppNavigators) {
    ScreenRegistrationContext(backStackEntry, appNavigators).content()
  }
}

interface ScreenRegistration {
  val screen: NavScreen
  @Composable fun content(backStackEntry: NavBackStackEntry, appNavigators: AppNavigators)
}

class ScreenRegistrationContext(val backStackEntry: NavBackStackEntry, val appNavigators: AppNavigators)
