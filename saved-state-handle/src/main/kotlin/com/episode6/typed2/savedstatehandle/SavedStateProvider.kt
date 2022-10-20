package com.episode6.typed2.savedstatehandle

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.episode6.typed2.bundles.BundleKey

fun <T: Any?> SavedStateHandle.setSavedStateProvider(key: BundleKey<T, Bundle?>, provider: () -> T) {
  setSavedStateProvider(key.name) { key.mapper.mapSet(provider())!! }
}

fun SavedStateHandle.clearSavedStateProvider(key: BundleKey<*, Bundle?>) {
  clearSavedStateProvider(key.name)
}
