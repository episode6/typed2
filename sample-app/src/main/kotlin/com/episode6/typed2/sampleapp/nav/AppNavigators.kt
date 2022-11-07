package com.episode6.typed2.sampleapp.nav

import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope

data class AppNavigators(
  val navController: NavController,
  val scope: CoroutineScope,
)

fun interface GoUpNavigator { fun go() }
fun AppNavigators.goUp(): GoUpNavigator = GoUpNavigator { navController.navigateUp() }
