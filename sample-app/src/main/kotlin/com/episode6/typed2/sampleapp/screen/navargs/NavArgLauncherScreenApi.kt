package com.episode6.typed2.sampleapp.screen.navargs

import com.episode6.typed2.navigation.compose.NavScreen
import com.episode6.typed2.navigation.compose.navigateTo
import com.episode6.typed2.sampleapp.nav.AppNavigators

object NavArgLauncherScreen : NavScreen("navArgLauncherScreen")

fun interface NavArgLauncherScreenNavigator {
  fun go()
}
fun AppNavigators.navArgLauncherScreen() = NavArgLauncherScreenNavigator { navController.navigateTo(NavArgLauncherScreen) }
