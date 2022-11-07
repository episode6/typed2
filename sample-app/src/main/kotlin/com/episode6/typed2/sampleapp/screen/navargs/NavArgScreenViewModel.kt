package com.episode6.typed2.sampleapp.screen.navargs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.episode6.typed2.bundles.BundleKeyNamespace
import com.episode6.typed2.kotlinx.serialization.bundlizer.bundlized
import com.episode6.typed2.sampleapp.data.KtxSerializable
import com.episode6.typed2.savedstatehandle.get
import com.episode6.typed2.savedstatehandle.setSavedStateProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@HiltViewModel class NavArgScreenViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) : ViewModel() {

  private object Keys : BundleKeyNamespace() {
    val SAVED_STATE = key("viewState").bundlized(ViewState::serializer)
  }

  @Serializable data class ViewState(
    val string: String,
    val ktxSerializable: KtxSerializable,
    val regularDataClassStr: String, // can't use regular data class here because it's not ktx serializable
  )

  private val _state = MutableStateFlow<ViewState?>(null)
  val state = _state.asStateFlow()

  init {
    viewModelScope.launch {
      _state.value = savedStateHandle.get(Keys.SAVED_STATE) ?: savedStateHandle.createViewStateFromArgs()
      savedStateHandle.setSavedStateProvider(Keys.SAVED_STATE) { state.value }
    }
  }

  fun setString(string: String) = _state.update { it?.copy(string = string) }
  fun setKtxSerializable(string: String) = _state.update { it?.copy(ktxSerializable = KtxSerializable(string)) }
  fun setRegularDataClassStr(string: String) = _state.update { it?.copy(regularDataClassStr = string) }
}

private suspend fun SavedStateHandle.createViewStateFromArgs() = NavArgScreenViewModel.ViewState(
  string = get(NavArgScreen.STRING),
  ktxSerializable = get(NavArgScreen.KTX_SERIALIZABLE),
  regularDataClassStr = get(NavArgScreen.REGULAR_DATA_CLASS).content
)
