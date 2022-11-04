package com.episode6.typed2.sampleapp.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.episode6.typed2.navigation.compose.NavScreen

class ScreenRegistration(
  val screen: NavScreen,
  val content: @Composable ScreenRegistrationContext.() -> Unit,
)

data class ScreenRegistrationContext(val backStackEntry: NavBackStackEntry, val appNavigators: AppNavigators)
