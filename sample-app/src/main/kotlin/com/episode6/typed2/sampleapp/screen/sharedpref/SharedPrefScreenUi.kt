package com.episode6.typed2.sampleapp.screen.sharedpref

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.episode6.typed2.sampleapp.nav.GoUpNavigator
import com.episode6.typed2.sampleapp.nav.ScreenRegistration
import com.episode6.typed2.sampleapp.nav.goUp
import com.episode6.typed2.sampleapp.ui.theme.AppScaffold
import com.episode6.typed2.sampleapp.ui.theme.BackButton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.multibindings.IntoSet
import javax.inject.Inject

@Module @InstallIn(ViewModelComponent::class) object SharedPrefScreenModule {
  @Provides @IntoSet fun sharedPrefScreen() = ScreenRegistration(SharedPrefScreen) {
    SharedPrefScreenUi(
      viewModel = hiltViewModel(),
      goUpNavigator = appNavigators.goUp(),
    )
  }
}

@Composable private fun SharedPrefScreenUi(
  viewModel: SharedPrefScreenViewModel,
  goUpNavigator: GoUpNavigator,
) = AppScaffold(
  title = "Shared Pref Example",
  navigationIcon = { BackButton(goUpNavigator) }
) {
  Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

  }
}

@HiltViewModel class SharedPrefScreenViewModel @Inject constructor(val sharedPrefs: SharedPreferences) : ViewModel()
