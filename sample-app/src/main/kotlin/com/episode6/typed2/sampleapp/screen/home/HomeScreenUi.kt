package com.episode6.typed2.sampleapp.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.episode6.typed2.sampleapp.nav.ScreenRegistration
import com.episode6.typed2.sampleapp.screen.navargs.NavArgLauncherScreenNavigator
import com.episode6.typed2.sampleapp.screen.navargs.NavArgScreenNavigator
import com.episode6.typed2.sampleapp.screen.navargs.navArgLauncherScreen
import com.episode6.typed2.sampleapp.screen.navargs.navArgScreen
import com.episode6.typed2.sampleapp.screen.sharedpref.SharedPrefScreenNavigator
import com.episode6.typed2.sampleapp.screen.sharedpref.sharedPrefScreen
import com.episode6.typed2.sampleapp.ui.theme.AppScaffold
import com.episode6.typed2.sampleapp.ui.theme.FullWidthButton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module @InstallIn(ViewModelComponent::class)
object HomeScreenModule {
  @Provides @IntoSet fun homeScreen() = ScreenRegistration(HomeScreen) {
    HomeScreenUI(
      sharedPrefScreenNavigator = appNavigators.sharedPrefScreen(),
      navArgLauncherScreenNavigator = appNavigators.navArgLauncherScreen(),
    )
  }
}

@Composable private fun HomeScreenUI(
  sharedPrefScreenNavigator: SharedPrefScreenNavigator,
  navArgLauncherScreenNavigator: NavArgLauncherScreenNavigator,
) = AppScaffold {
  Column(modifier = Modifier
    .padding(8.dp)
    .verticalScroll(rememberScrollState())) {
    Text(text = "Hello there", modifier = Modifier
      .padding(8.dp)
      .testTag("TEST_TAG"))

    FullWidthButton(text = "Shared Pref Sample", onClick = sharedPrefScreenNavigator::go)
    FullWidthButton(text = "Nav Arg Sample", onClick = navArgLauncherScreenNavigator::go)
  }
}
