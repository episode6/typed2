package com.episode6.typed2.sampleapp.screen.sharedpref

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.episode6.typed2.sampleapp.nav.GoUpNavigator
import com.episode6.typed2.sampleapp.nav.ScreenRegistration
import com.episode6.typed2.sampleapp.nav.goUp
import com.episode6.typed2.sampleapp.ui.theme.AppScaffold
import com.episode6.typed2.sampleapp.ui.theme.BackButton
import com.episode6.typed2.sharedprefs.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet
import kotlinx.coroutines.flow.*

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
  Column(modifier = Modifier
    .verticalScroll(rememberScrollState())
    .padding(8.dp)) {
    val string by viewModel.stringFlow.collectAsState()
    val ktx by viewModel.ktxFlow.collectAsState()
    val regular by viewModel.regularFlow.collectAsState()
    TextCard(
      value = string ?: "",
      onValueChange = { viewModel.setString(it) },
      label = "String pref",
    )
    TextCard(
      value = ktx?.content ?: "",
      onValueChange = { viewModel.setKtxSerializableContent(it) },
      label = "KtxSerializable pref",
    )
    TextCard(
      value = regular?.content ?: "",
      onValueChange = { viewModel.setRegularDataClassContent(it) },
      label = "Regular Data Class pref",
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable private fun TextCard(
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
) = OutlinedTextField(
  value = value,
  onValueChange = onValueChange,
  label = { Text(text = label) },
  keyboardOptions = KeyboardOptions(autoCorrect = false)
)
