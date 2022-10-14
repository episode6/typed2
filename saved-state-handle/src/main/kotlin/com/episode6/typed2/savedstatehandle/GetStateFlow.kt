package com.episode6.typed2.savedstatehandle

import androidx.lifecycle.SavedStateHandle
import com.episode6.typed2.AsyncKey
import com.episode6.typed2.Key
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

fun <T, BACKED_BY> SavedStateHandle.getStateFlow(
  scope: CoroutineScope,
  key: Key<T, *, *, BACKED_BY>,
): StateFlow<T> = getStateFlow(key.name, key.backingDefault()).run {
  map { key.mapGet(it) }.stateIn(scope, SharingStarted.Eagerly, initialValue = key.mapGet(value))
}

fun <T, BACKED_BY> SavedStateHandle.getStateFlow(
  scope: CoroutineScope,
  key: AsyncKey<T, *, *, BACKED_BY>,
): StateFlow<T?> = getStateFlow(key.name, key.backingDefault())
  .map { key.mapGet(it) }
  .stateIn(scope, SharingStarted.Eagerly, null)


