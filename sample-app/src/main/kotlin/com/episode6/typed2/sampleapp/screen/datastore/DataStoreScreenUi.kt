package com.episode6.typed2.sampleapp.screen.datastore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.episode6.typed2.async
import com.episode6.typed2.datastore.preferences.*
import com.episode6.typed2.gson.gson
import com.episode6.typed2.kotlinx.serialization.json.json
import com.episode6.typed2.sampleapp.data.KtxSerializable
import com.episode6.typed2.sampleapp.data.RegularAssDataClass
import com.episode6.typed2.sampleapp.nav.GoUpNavigator
import com.episode6.typed2.sampleapp.nav.ScreenRegistration
import com.episode6.typed2.sampleapp.nav.goUp
import com.episode6.typed2.sampleapp.ui.theme.AppScaffold
import com.episode6.typed2.sampleapp.ui.theme.BackButton
import com.episode6.typed2.sampleapp.ui.theme.TextCard
import com.episode6.typed2.sampleapp.util.defaultScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.multibindings.IntoSet
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@Module @InstallIn(ViewModelComponent::class) object DataStoreScreenModule {
  @Provides @IntoSet fun dataStoreScreen() = ScreenRegistration(DataStoreScreen) {
    DataStoreScreenUi(
      viewModel = hiltViewModel(),
      goUpNavigator = appNavigators.goUp(),
    )
  }
}

@HiltViewModel class DataStoreScreenViewModel @Inject constructor(dataStore: DataStore<Preferences>) : ViewModel() {
  private object DataKeys : DataStoreKeyNamespace() {
    val STRING = key("string").string()
    val KTX_SERIALIZABLE = key("ktxSerializable").json(KtxSerializable::serializer).async()
    val REGULAR_DATA_CLASS = key("dataClass").gson<RegularAssDataClass>().async()
  }

  val stringState = dataStore.mutableStateFlow(DataKeys.STRING, defaultScope, debounceWrites = 250.milliseconds)
  val ktxState = dataStore.mutableStateFlow(DataKeys.KTX_SERIALIZABLE, defaultScope, debounceWrites = 250.milliseconds)
  val regularState = dataStore.mutableStateFlow(DataKeys.REGULAR_DATA_CLASS, defaultScope, debounceWrites = 250.milliseconds)
}

@Composable private fun DataStoreScreenUi(
  viewModel: DataStoreScreenViewModel,
  goUpNavigator: GoUpNavigator,
) = AppScaffold(
  title = "DataStore Example",
  navigationIcon = { BackButton(goUpNavigator) }
) {
  Column(modifier = Modifier
    .verticalScroll(rememberScrollState())
    .padding(8.dp)) {

    Text(
      text = "The fields below are all backed by a Preferences DataStore using typed2 DataStoreKeys.",
      style = MaterialTheme.typography.bodyLarge
    )
    Spacer(modifier = Modifier.height(8.dp))

    // emissions are wrapped in DataStoreValue; Uninitialized means the store hasn't been read yet
    val string by viewModel.stringState.collectAsState()
    val ktx by viewModel.ktxState.collectAsState()
    val regular by viewModel.regularState.collectAsState()
    TextCard(
      value = string.valueOrNull ?: "",
      onValueChange = { viewModel.stringState.value = DataStoreValue.Loaded(it) },
      label = "String entry",
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextCard(
      value = ktx.valueOrNull?.content ?: "",
      onValueChange = { viewModel.ktxState.value = DataStoreValue.Loaded(KtxSerializable(it)) },
      label = "KtxSerializable entry",
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextCard(
      value = regular.valueOrNull?.content ?: "",
      onValueChange = { viewModel.regularState.value = DataStoreValue.Loaded(RegularAssDataClass(it)) },
      label = "Regular Data Class entry",
    )
  }
}
