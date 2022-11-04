package com.episode6.typed2.sampleapp.screen.home

import com.episode6.typed2.navigation.compose.NavScreen
import com.episode6.typed2.navigation.compose.navigateTo
import com.episode6.typed2.sampleapp.nav.AppNavigators

object HomeScreen : NavScreen("home")

fun interface HomeScreenNavigator { fun go() }
fun AppNavigators.homeScreen(): HomeScreenNavigator = HomeScreenNavigator { navController.navigateTo(HomeScreen) }
