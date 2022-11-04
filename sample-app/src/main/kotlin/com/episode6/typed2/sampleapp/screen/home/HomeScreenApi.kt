package com.episode6.typed2.sampleapp.screen.home

import com.episode6.typed2.navigation.compose.NavScreen
import com.episode6.typed2.navigation.compose.navigateTo
import com.episode6.typed2.sampleapp.nav.AppNavigators

object HomeScreen : NavScreen("home")

typealias HomeScreenNavigator = ()->Unit
fun AppNavigators.homeScreen(): HomeScreenNavigator {
  return { navController.navigateTo(HomeScreen) }
}
