package com.episode6.typed2.sampleapp.screen.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.episode6.typed2.sampleapp.nav.ScreenRegistration
import com.episode6.typed2.sampleapp.ui.theme.AppScaffold
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module @InstallIn(ViewModelComponent::class)
object HomeScreenModule {
  @Provides @IntoSet fun homeScreen() = ScreenRegistration(HomeScreen) {
    HomeScreenUI()
  }
}

@Composable private fun HomeScreenUI() = AppScaffold {
  Text(text = "Hello there", modifier = Modifier
    .padding(8.dp)
    .testTag("TEST_TAG"))
}
