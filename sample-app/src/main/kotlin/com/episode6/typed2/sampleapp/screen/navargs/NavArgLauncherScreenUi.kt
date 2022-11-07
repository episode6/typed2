package com.episode6.typed2.sampleapp.screen.navargs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.episode6.typed2.sampleapp.data.KtxSerializable
import com.episode6.typed2.sampleapp.data.RegularAssDataClass
import com.episode6.typed2.sampleapp.nav.GoUpNavigator
import com.episode6.typed2.sampleapp.nav.ScreenRegistration
import com.episode6.typed2.sampleapp.nav.goUp
import com.episode6.typed2.sampleapp.ui.theme.AppScaffold
import com.episode6.typed2.sampleapp.ui.theme.BackButton
import com.episode6.typed2.sampleapp.ui.theme.FullWidthButton
import com.episode6.typed2.sampleapp.ui.theme.TextCard
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module @InstallIn(ViewModelComponent::class) object NavArgLauncherScreenModule {
  @Provides @IntoSet fun screenReg() = ScreenRegistration(NavArgLauncherScreen) {
    NavArgLauncherScreenUI(goUpNavigator = appNavigators.goUp(), navArgScreenNavigator = appNavigators.navArgScreen())
  }
}

@Composable private fun NavArgLauncherScreenUI(
  goUpNavigator: GoUpNavigator,
  navArgScreenNavigator: NavArgScreenNavigator,
) = AppScaffold(
  title = "Nav Argument Example",
  navigationIcon = { BackButton(goUpNavigator) }
) {
  Column(modifier = Modifier
    .verticalScroll(rememberScrollState())
    .padding(8.dp)) {

    Text(
      text = "The fields below will all be passed to the next screen as nav arguments.",
      style = MaterialTheme.typography.bodyLarge
    )
    Spacer(modifier = Modifier.height(8.dp))

    var string by rememberSaveable { mutableStateOf("") }
    var ktxSerializableStr by rememberSaveable { mutableStateOf("") }
    var regularDataClassStr by rememberSaveable { mutableStateOf("") }

    TextCard(
      value = string,
      onValueChange = { string = it },
      label = "String arg",
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextCard(
      value = ktxSerializableStr,
      onValueChange = { ktxSerializableStr = it },
      label = "KtxSerializable arg",
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextCard(
      value = regularDataClassStr,
      onValueChange = { regularDataClassStr = it },
      label = "Regular Data Class arg",
    )

    Spacer(modifier = Modifier.height(8.dp))
    FullWidthButton(
      text = "Go to next screen",
      onClick = {
        navArgScreenNavigator.go(
          string = string,
          ktxSerializable = KtxSerializable(ktxSerializableStr),
          regularDataClass = RegularAssDataClass(regularDataClassStr),
        )
      }
    )
  }
}
