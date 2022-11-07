@file:OptIn(ExperimentalCoroutinesApi::class)

package com.episode6.typed2.sharedprefs

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration

fun <T> SharedPreferences.flow(key: PrefKey<T, *>): Flow<T> =
  changedKeyNames()
    .filter { it == key.name }
    .mapLatest { get(key) }
    .onStart { emit(get(key)) }
    .distinctUntilChanged()

fun <T> SharedPreferences.flow(key: AsyncPrefKey<T, *>): Flow<T> =
  changedKeyNames()
    .filter { it == key.name }
    .mapLatest { get(key) }
    .onStart { emit(get(key)) }
    .distinctUntilChanged()


@FlowPreview
fun <T> SharedPreferences.mutableStateFlow(
  key: PrefKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T> = MutableStateFlow(get(key)).also { mutable ->
  scope.launch { // write changes to backing storage
    mutable.debounce(debounceWrites).filter { it != get(key) }.collectLatest { edit(commit = true) { set(key, it) } }
  }
  scope.launch { // update mutable with the latest from backing storage
    flow(key).collectLatest { mutable.value = it }
  }
}

@FlowPreview
fun <T> SharedPreferences.mutableStateFlow(
  key: AsyncPrefKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T?> = MutableStateFlow<T?>(null).also { mutable ->
  scope.launch { // write changes to backing storage
    mutable.filterNotNull().debounce(debounceWrites).filter { it != get(key) }.collectLatest { edit(commit = true) { set(key, it) } }
  }
  scope.launch { // remove key when set to null
    mutable.drop(1).filter { it == null }.collectLatest {
      edit(commit = true) { remove(key) }
      mutable.value = get(key)
    }
  }
  scope.launch { // update mutable with the latest from backing storage
    flow(key).collectLatest { mutable.value = it }
  }
}

private fun SharedPreferences.changedKeyNames(): Flow<String> = callbackFlow {
  val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, name -> trySend(name) }
  registerOnSharedPreferenceChangeListener(listener)
  awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
}
