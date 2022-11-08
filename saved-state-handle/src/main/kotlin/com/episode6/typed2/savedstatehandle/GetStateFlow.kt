package com.episode6.typed2.savedstatehandle

import androidx.lifecycle.SavedStateHandle
import com.episode6.typed2.AsyncKey
import com.episode6.typed2.DelegateAsyncMutableStateFlow
import com.episode6.typed2.DelegateMutableStateFlow
import com.episode6.typed2.Key
import com.episode6.typed2.bundles.AsyncBundleKey
import com.episode6.typed2.bundles.BundleKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlin.time.Duration

fun <T, BACKED_BY> SavedStateHandle.flow(key: Key<T, BACKED_BY, *, *>): Flow<T> = getStateFlow(key.name, key.backingTypeInfo.default)
  .map { key.mapper.mapGet(it) }

fun <T, BACKED_BY> SavedStateHandle.flow(key: AsyncKey<T, BACKED_BY, *, *>): Flow<T> = getStateFlow(key.name, key.backingTypeInfo.default)
  .map { key.mapper.mapGet(it) }

fun <T, BACKED_BY> SavedStateHandle.getStateFlow(
  key: Key<T, BACKED_BY, *, *>,
  scope: CoroutineScope,
  started: SharingStarted,
): StateFlow<T> = getStateFlow(key.name, key.backingTypeInfo.default).run {
  map { key.mapper.mapGet(it) }.stateIn(scope, started, initialValue = key.mapper.mapGet(value))
}

fun <T, BACKED_BY> SavedStateHandle.getStateFlow(
  key: AsyncKey<T, BACKED_BY, *, *>,
  scope: CoroutineScope,
  started: SharingStarted,
): StateFlow<T?> = flow(key).stateIn(scope, started, null)

fun <T> SavedStateHandle.mutableStateFlow(
  key: BundleKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T> = DelegateMutableStateFlow(
  scope = scope,
  debounceWrites = debounceWrites,
  get = { get(key) },
  set = { set(key, it) },
  updates = flow(key)
)

fun <T> SavedStateHandle.mutableStateFlow(
  key: AsyncBundleKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T?> = DelegateAsyncMutableStateFlow(
  scope = scope,
  debounceWrites = debounceWrites,
  get = { get(key) },
  set = { set(key, it) },
  remove = { remove(key) },
  updates = flow(key)
)
