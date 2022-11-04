package com.episode6.typed2.sampleapp.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.episode6.typed2.navigation.compose.NavHost
import com.episode6.typed2.navigation.compose.composableScreen
import com.episode6.typed2.sampleapp.screen.home.HomeScreen

@Composable fun AppNavGraph(screens: Set<ScreenRegistration>, navController: NavHostController = rememberNavController()) {
  val navigators = AppNavigators(navController, rememberCoroutineScope())
  NavHost(navController = navController, startScreen = HomeScreen) {
    screens.forEach { reg ->
      composableScreen(reg.screen) { reg.content(ScreenRegistrationContext(it, navigators)) }
    }
  }
}
