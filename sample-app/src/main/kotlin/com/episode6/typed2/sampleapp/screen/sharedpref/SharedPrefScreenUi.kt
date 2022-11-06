package com.episode6.typed2.sampleapp.screen.sharedpref

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
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
import com.episode6.typed2.sampleapp.ui.theme.FullWidthButton
import com.episode6.typed2.sharedprefs.PrefKeyNamespace
import com.episode6.typed2.sharedprefs.get
import com.episode6.typed2.sharedprefs.launchEdit
import com.episode6.typed2.sharedprefs.set
import com.episode6.typed2.string
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.multibindings.IntoSet
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
  Column(modifier = Modifier
    .verticalScroll(rememberScrollState())
    .padding(8.dp)) {
    val viewState by viewModel.state.collectAsState()
    TextCard(
      value = viewState.string ?: "",
      onValueChange = { viewModel.setString(it) },
      label = "String pref",
    )
    TextCard(
      value = viewState.ktxSerializable?.content ?: "",
      onValueChange = { viewModel.setKtxSerializableContent(it) },
      label = "KtxSerializable pref",
    )
    TextCard(
      value = viewState.regularDataClass?.content ?: "",
      onValueChange = { viewModel.setRegularDataClassContent(it) },
      label = "Regular Data Class pref",
    )
    FullWidthButton(text = "Save", onClick = viewModel::savePrefs)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable private fun TextCard(
  value: String,
  onValueChange: (String) -> Unit,
  label: String) = OutlinedTextField(value = value, onValueChange = onValueChange, label = { Text(text = label)})

@HiltViewModel class SharedPrefScreenViewModel @Inject constructor(private val sharedPrefs: SharedPreferences) : ViewModel() {
  private object PrefKeys : PrefKeyNamespace() {
    val STRING = key("string").string()
    val KTX_SERIALIZABLE = key("ktxSerializable").json(KtxSerializable::serializer).async()
    val REGULAR_DATA_CLASS = key("dataClass").gson<RegularAssDataClass>().async()
  }

  data class ViewState(
    val string: String? = null,
    val ktxSerializable: KtxSerializable? = null,
    val regularDataClass: RegularAssDataClass? = null,
  )

  private val _state = MutableStateFlow(ViewState(string = sharedPrefs.get(PrefKeys.STRING)))
  val state: StateFlow<ViewState> = _state.asStateFlow()

  init {
    viewModelScope.launch {
      _state.update {
        it.copy(
          ktxSerializable = sharedPrefs.get(PrefKeys.KTX_SERIALIZABLE),
          regularDataClass = sharedPrefs.get(PrefKeys.REGULAR_DATA_CLASS)
        )
      }
    }
  }

  fun savePrefs() {
    Log.e("GHACKETT", "SAVING PREFS")
    val state = _state.value
    sharedPrefs.launchEdit(viewModelScope) {
      set(PrefKeys.STRING, state.string)
      set(PrefKeys.KTX_SERIALIZABLE, state.ktxSerializable)
      set(PrefKeys.REGULAR_DATA_CLASS, state.regularDataClass)
    }
  }

  fun setString(string: String) = _state.update { it.copy(string = string) }
  fun setKtxSerializableContent(content: String) = _state.update { it.copy(ktxSerializable = KtxSerializable(content)) }
  fun setRegularDataClassContent(content: String) = _state.update { it.copy(regularDataClass = RegularAssDataClass(content)) }
}
