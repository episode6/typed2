package com.episode6.typed2.sampleapp.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.episode6.typed2.navigation.compose.NavHost
import com.episode6.typed2.navigation.compose.composableScreen
import com.episode6.typed2.sampleapp.screen.home.HomeScreen

@Composable fun AppNavGraph(navController: NavHostController, screens: Set<ScreenRegistration>) {
  val scope = rememberCoroutineScope()
  val context = LocalContext.current
  val navigators = AppNavigators(navController, scope, context)
  NavHost(navController = navController, startScreen = HomeScreen) {
    screens.forEach { reg ->
      composableScreen(reg.screen) { reg.content(it, navigators) }
    }
  }
}
