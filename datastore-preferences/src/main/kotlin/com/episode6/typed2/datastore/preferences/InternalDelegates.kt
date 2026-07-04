@file:OptIn(FlowPreview::class)

package com.episode6.typed2.datastore.preferences

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration

// datastore-specific take on core's DelegateAsyncMutableStateFlow: emissions are wrapped in
// DataStoreValue so consumers can distinguish the uninitialized state from an absent key, and
// there is no explicit remove path because DataStore treats null writes as removal
@Suppress("FunctionName")
internal fun <T> DelegateDataStoreMutableStateFlow(
  scope: CoroutineScope,
  debounceWrites: Duration,
  get: suspend () -> T,
  set: suspend (T) -> Unit,
  updates: Flow<T>,
): MutableStateFlow<DataStoreValue<T>> = MutableStateFlow<DataStoreValue<T>>(DataStoreValue.Uninitialized).also { mutable ->
  // write loaded values to backing storage (setting Uninitialized is a no-op on the store)
  mutable.filterIsInstance<DataStoreValue.Loaded<T>>().debounce(debounceWrites).collectLatestIn(scope) {
    if (it.value != get()) set(it.value)
  }
  // update mutable with the latest from backing storage
  updates.collectLatestIn(scope) { mutable.value = DataStoreValue.Loaded(it) }
}

private fun <T> Flow<T>.collectLatestIn(scope: CoroutineScope, action: suspend (value: T) -> Unit) {
  scope.launch { collectLatest(action) }
}
