package com.episode6.typed2.sampleapp.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.episode6.typed2.sampleapp.nav.ScreenRegistration
import com.episode6.typed2.sampleapp.screen.sharedpref.SharedPrefScreenNavigator
import com.episode6.typed2.sampleapp.screen.sharedpref.sharedPrefScreen
import com.episode6.typed2.sampleapp.ui.theme.AppScaffold
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module @InstallIn(ViewModelComponent::class)
object HomeScreenModule {
  @Provides @IntoSet fun homeScreen() = ScreenRegistration(HomeScreen) {
    HomeScreenUI(
      sharedPrefScreenNavigator = appNavigators.sharedPrefScreen()
    )
  }
}

@Composable private fun HomeScreenUI(
  sharedPrefScreenNavigator: SharedPrefScreenNavigator,
) = AppScaffold {
  Column(modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState())) {
    Text(text = "Hello there", modifier = Modifier
      .padding(8.dp)
      .testTag("TEST_TAG"))
    NavButton(text = "Shared Pref Sample", onClick = sharedPrefScreenNavigator::go)
  }
}

@Composable private fun NavButton(text: String, onClick: () -> Unit) {
  Button(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
    Box(contentAlignment = Alignment.Center) {
      Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
  }
}
