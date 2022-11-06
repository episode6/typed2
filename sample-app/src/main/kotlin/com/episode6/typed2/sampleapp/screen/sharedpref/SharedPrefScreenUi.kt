package com.episode6.typed2.sampleapp.screen.sharedpref

import android.content.SharedPreferences
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.episode6.typed2.async
import com.episode6.typed2.gson.gson
import com.episode6.typed2.kotlinx.serialization.json.json
import com.episode6.typed2.sampleapp.data.KtxSerializable
import com.episode6.typed2.sampleapp.data.RegularAssDataClass
import com.episode6.typed2.sampleapp.nav.GoUpNavigator
import com.episode6.typed2.sampleapp.nav.ScreenRegistration
import com.episode6.typed2.sampleapp.nav.goUp
import com.episode6.typed2.sampleapp.ui.theme.AppScaffold
import com.episode6.typed2.sampleapp.ui.theme.BackButton
import com.episode6.typed2.sharedprefs.*
import com.episode6.typed2.string
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.multibindings.IntoSet
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@Module @InstallIn(ViewModelComponent::class) object SharedPrefScreenModule {
  @Provides @IntoSet fun sharedPrefScreen() = ScreenRegistration(SharedPrefScreen) {
    SharedPrefScreenUi(
      viewModel = hiltViewModel(),
      goUpNavigator = appNavigators.goUp(),
    )
  }
}

@HiltViewModel class SharedPrefScreenViewModel @Inject constructor(sharedPrefs: SharedPreferences) : ViewModel() {
  private object PrefKeys : PrefKeyNamespace() {
    val STRING = key("string").string()
    val KTX_SERIALIZABLE = key("ktxSerializable").json(KtxSerializable::serializer).async()
    val REGULAR_DATA_CLASS = key("dataClass").gson<RegularAssDataClass>().async()
  }

  val stringState = sharedPrefs.mutableStateFlow(PrefKeys.STRING, viewModelScope)
  val ktxState = sharedPrefs.mutableStateFlow(PrefKeys.KTX_SERIALIZABLE, viewModelScope)
  val regularState = sharedPrefs.mutableStateFlow(PrefKeys.REGULAR_DATA_CLASS, viewModelScope)
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
    val string by viewModel.stringState.collectAsState()
    val ktx by viewModel.ktxState.collectAsState()
    val regular by viewModel.regularState.collectAsState()
    TextCard(
      value = string ?: "",
      onValueChange = { viewModel.stringState.value = it },
      label = "String pref",
    )
    TextCard(
      value = ktx?.content ?: "",
      onValueChange = { viewModel.ktxState.value = KtxSerializable(it) },
      label = "KtxSerializable pref",
    )
    TextCard(
      value = regular?.content ?: "",
      onValueChange = { viewModel.regularState.value = RegularAssDataClass(it) },
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
  keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Ascii)
)
