package com.episode6.typed2.savedstatehandle

import androidx.lifecycle.SavedStateHandle
import com.episode6.typed2.AsyncKey
import com.episode6.typed2.Key
import com.episode6.typed2.bundles.AsyncBundleKey
import com.episode6.typed2.bundles.BundleKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration

fun <T, BACKED_BY> SavedStateHandle.flow(key: Key<T, BACKED_BY, *, *>,): Flow<T> = getStateFlow(key.name, key.backingTypeInfo.default)
  .map { key.mapper.mapGet(it) }

fun <T, BACKED_BY> SavedStateHandle.flow(key: AsyncKey<T, BACKED_BY, *, *>,): Flow<T> = getStateFlow(key.name, key.backingTypeInfo.default)
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

@FlowPreview
fun <T> SavedStateHandle.mutableStateFlow(
  key: BundleKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T> = MutableStateFlow(get(key)).also { mutable ->
  scope.launch { // write changes to backing storage
    mutable.debounce(debounceWrites).filter { it != get(key) }.collectLatest { set(key, it) }
  }
  scope.launch { // update mutable with the latest from backing storage
    flow(key).collectLatest { mutable.value = it }
  }
}

@FlowPreview
fun <T> SavedStateHandle.mutableStateFlow(
  key: AsyncBundleKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T?> = MutableStateFlow<T?>(null).also { mutable ->
  scope.launch { // write changes to backing storage
    mutable.filterNotNull().debounce(debounceWrites).filter { it != get(key) }.collectLatest { set(key, it) }
  }
  scope.launch { // remove key when set to null
    mutable.drop(1).filter { it == null }.collectLatest {
      remove(key)
      mutable.value = get(key)
    }
  }
  scope.launch { // update mutable with the latest from backing storage
    flow(key).collectLatest { mutable.value = it }
  }
}
