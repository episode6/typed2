package com.episode6.typed2.sampleapp.screen.navargs

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.episode6.typed2.sampleapp.data.KtxSerializable
import com.episode6.typed2.sampleapp.data.RegularAssDataClass
import com.episode6.typed2.sampleapp.nav.GoUpNavigator
import com.episode6.typed2.sampleapp.nav.ScreenRegistration
import com.episode6.typed2.sampleapp.nav.goUp
import com.episode6.typed2.sampleapp.ui.theme.AppScaffold
import com.episode6.typed2.sampleapp.ui.theme.BackButton
import com.episode6.typed2.sampleapp.ui.theme.TextCard
import com.episode6.typed2.savedstatehandle.mutableStateFlow
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.multibindings.IntoSet
import javax.inject.Inject

@Module @InstallIn(ViewModelComponent::class) object NavArgScreenModule {
  @Provides @IntoSet fun screenReg() = ScreenRegistration(NavArgScreen) {
    NavArgScreenUI(viewModel = hiltViewModel(), goUpNavigator = appNavigators.goUp())
  }
}

@HiltViewModel class NavArgScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
  val stringState = savedStateHandle.mutableStateFlow(NavArgScreen.STRING, viewModelScope)
  val ktxSerializableState = savedStateHandle.mutableStateFlow(NavArgScreen.KTX_SERIALIZABLE, viewModelScope)
  val regularDataClassState = savedStateHandle.mutableStateFlow(NavArgScreen.REGULAR_DATA_CLASS, viewModelScope)
}

@Composable private fun NavArgScreenUI(
  viewModel: NavArgScreenViewModel,
  goUpNavigator: GoUpNavigator,
) = AppScaffold(
  title = "Nav Argument Example (results)",
  navigationIcon = { BackButton(goUpNavigator) }
) {
  Column(modifier = Modifier
    .verticalScroll(rememberScrollState())
    .padding(8.dp)) {

    Text(
      text = "The arguments from the previous screen are shown below. If changes are made they will be stored in our SavedStateHandle.",
      style = MaterialTheme.typography.bodyLarge
    )
    Spacer(modifier = Modifier.height(8.dp))

    val string by viewModel.stringState.collectAsState()
    val ktxSerializable by viewModel.ktxSerializableState.collectAsState()
    val regularDataClass by viewModel.regularDataClassState.collectAsState()

    TextCard(
      value = string,
      onValueChange = { viewModel.stringState.value = it },
      label = "String arg",
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextCard(
      value = ktxSerializable?.content ?: "",
      onValueChange = { viewModel.ktxSerializableState.value = KtxSerializable(it) },
      label = "KtxSerializable arg",
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextCard(
      value = regularDataClass?.content ?: "",
      onValueChange = { viewModel.regularDataClassState.value = RegularAssDataClass(it) },
      label = "Regular Data Class arg",
    )
  }
}
