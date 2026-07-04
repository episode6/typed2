@file:OptIn(ExperimentalCoroutinesApi::class)

package com.episode6.typed2.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.time.Duration

public fun <T> DataStore<Preferences>.flow(key: DataStoreKey<T, *>): Flow<T> = data
  // data re-emits full snapshots on every edit; skip re-running the key's mapper unless our raw value changed
  .distinctUntilChangedBy { prefs -> prefs.asMap().entries.firstOrNull { it.key.name == key.name }?.value }
  .mapLatest { it.typed().get(key) }
  .distinctUntilChanged()

/**
 * Returns a [MutableStateFlow] that starts as [DataStoreValue.Uninitialized], reflects the latest value
 * of [key] as [DataStoreValue.Loaded] emissions, and persists any [DataStoreValue.Loaded] values set on it
 * (a loaded null value removes the entry; setting [DataStoreValue.Uninitialized] is a no-op on the store).
 */
public fun <T> DataStore<Preferences>.mutableStateFlow(
  key: DataStoreKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<DataStoreValue<T>> = DelegateDataStoreMutableStateFlow(
  scope = scope,
  debounceWrites = debounceWrites,
  get = { get(key) },
  set = { set(key, it) },
  updates = flow(key),
)
