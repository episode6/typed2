package com.episode6.typed2.sampleapp.screen.sharedpref

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.episode6.typed2.async
import com.episode6.typed2.gson.gson
import com.episode6.typed2.kotlinx.serialization.json.json
import com.episode6.typed2.sampleapp.data.KtxSerializable
import com.episode6.typed2.sampleapp.data.RegularAssDataClass
import com.episode6.typed2.sharedprefs.PrefKeyNamespace
import com.episode6.typed2.sharedprefs.launchEdit
import com.episode6.typed2.sharedprefs.set
import com.episode6.typed2.sharedprefs.stateFlow
import com.episode6.typed2.string
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Inject

@HiltViewModel class SharedPrefScreenViewModel @Inject constructor(private val sharedPrefs: SharedPreferences) : ViewModel() {
  private object PrefKeys : PrefKeyNamespace() {
    val STRING = key("string").string()
    val KTX_SERIALIZABLE = key("ktxSerializable").json(KtxSerializable::serializer).async()
    val REGULAR_DATA_CLASS = key("dataClass").gson<RegularAssDataClass>().async()
  }

  val stringFlow = sharedPrefs.stateFlow(PrefKeys.STRING, viewModelScope, SharingStarted.Eagerly)
  val ktxFlow = sharedPrefs.stateFlow(PrefKeys.KTX_SERIALIZABLE, viewModelScope, SharingStarted.Eagerly)
  val regularFlow = sharedPrefs.stateFlow(PrefKeys.REGULAR_DATA_CLASS, viewModelScope, SharingStarted.Eagerly)

  fun setString(string: String) = sharedPrefs.edit { set(PrefKeys.STRING, string) }

  fun setKtxSerializableContent(content: String) =
    sharedPrefs.launchEdit(viewModelScope) { set(PrefKeys.KTX_SERIALIZABLE, KtxSerializable(content)) }

  fun setRegularDataClassContent(content: String) =
    sharedPrefs.launchEdit(viewModelScope) { set(PrefKeys.REGULAR_DATA_CLASS, RegularAssDataClass(content)) }
}
