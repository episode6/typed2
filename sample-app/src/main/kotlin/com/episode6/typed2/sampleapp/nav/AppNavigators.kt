package com.episode6.typed2.sampleapp.nav

import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope

data class AppNavigators(
  val navController: NavController,
  val scope: CoroutineScope,
)

typealias GoUpNavigator = () -> Unit

fun AppNavigators.goUp(): GoUpNavigator {
  return { navController.navigateUp() }
}
