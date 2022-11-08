@file:OptIn(FlowPreview::class)

package com.episode6.typed2

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty
import kotlin.time.Duration

class DelegateProperty<T : Any?>(val get: () -> T, val set: (T) -> Unit) {
  constructor(mutableStateFlow: MutableStateFlow<T>) : this(get = { mutableStateFlow.value }, set = { mutableStateFlow.value = it })

  operator fun getValue(thisRef: Any?, property: KProperty<*>): T = get()
  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = set(value)
}

@Suppress("FunctionName")
fun <T> DelegateMutableStateFlow(
  scope: CoroutineScope,
  debounceWrites: Duration,
  get: () -> T,
  set: (T) -> Unit,
  updates: Flow<T>? = null,
): MutableStateFlow<T> = MutableStateFlow(get()).also { mutable ->
  // write changes to backing storage
  mutable.debounce(debounceWrites).collectLatestIn(scope) { if (it != get()) set(it) }
  // update mutable with the latest from backing storage if available
  updates?.collectLatestIn(scope) { mutable.value = it }
}

@Suppress("FunctionName")
fun <T> DelegateAsyncMutableStateFlow(
  scope: CoroutineScope,
  debounceWrites: Duration,
  get: suspend () -> T,
  set: suspend (T) -> Unit,
  remove: () -> Unit,
  updates: Flow<T>? = null,
): MutableStateFlow<T?> = MutableStateFlow<T?>(null).also { mutable ->
  // write non-nulls to backing storage
  mutable.filterNotNull().debounce(debounceWrites).collectLatestIn(scope) { if (it != get()) set(it) }
  // when a null is encountered, call the remove method then set the mutable with a fresh value
  mutable.drop(1).filter { it == null }.collectLatestIn(scope) {
    remove()
    mutable.value = get()
  }
  // update mutable with the latest from backing storage if available
  updates?.collectLatestIn(scope) { mutable.value = it }
}

private fun <T> Flow<T>.collectLatestIn(scope: CoroutineScope, action: suspend (value: T) -> Unit) {
  scope.launch { collectLatest(action) }
}
