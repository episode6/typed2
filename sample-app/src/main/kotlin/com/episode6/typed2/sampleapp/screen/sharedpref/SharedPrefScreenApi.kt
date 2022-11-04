package com.episode6.typed2.sampleapp.screen.sharedpref

import com.episode6.typed2.navigation.compose.NavScreen
import com.episode6.typed2.navigation.compose.navigateTo
import com.episode6.typed2.sampleapp.nav.AppNavigators

object SharedPrefScreen : NavScreen("prefs")

fun interface SharedPrefScreenNavigator { fun go() }
fun AppNavigators.sharedPrefScreen(): SharedPrefScreenNavigator = SharedPrefScreenNavigator { navController.navigateTo(SharedPrefScreen) }
