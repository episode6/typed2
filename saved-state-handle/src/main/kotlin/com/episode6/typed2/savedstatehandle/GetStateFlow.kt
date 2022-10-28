package com.episode6.typed2.savedstatehandle

import androidx.lifecycle.SavedStateHandle
import com.episode6.typed2.AsyncKey
import com.episode6.typed2.Key
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

fun <T, BACKED_BY> SavedStateHandle.stateFlow(
  key: Key<T, BACKED_BY, *, *>,
  scope: CoroutineScope,
  started: SharingStarted,
): StateFlow<T> = getStateFlow(key.name, key.backingTypeInfo.default).run {
  map { key.mapper.mapGet(it) }.stateIn(scope, started, initialValue = key.mapper.mapGet(value))
}

fun <T, BACKED_BY> SavedStateHandle.sharedFlow(
  key: AsyncKey<T, BACKED_BY, *, *>,
  scope: CoroutineScope,
  started: SharingStarted,
  replay: Int = 1,
): SharedFlow<T> = getStateFlow(key.name, key.backingTypeInfo.default)
  .map { key.mapper.mapGet(it) }
  .conflate()
  .shareIn(scope, started, replay)


