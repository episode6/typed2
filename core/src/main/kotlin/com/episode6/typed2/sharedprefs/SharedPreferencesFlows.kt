@file:OptIn(ExperimentalCoroutinesApi::class)

package com.episode6.typed2.sharedprefs

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

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

fun <T> SharedPreferences.stateFlow(key: PrefKey<T, *>, scope: CoroutineScope, started: SharingStarted): StateFlow<T> =
  flow(key).stateIn(scope, started, get(key))

fun <T> SharedPreferences.sharedFlow(key: AsyncPrefKey<T, *>, scope: CoroutineScope, started: SharingStarted, replay: Int = 1): SharedFlow<T> =
  flow(key)
    .conflate()
    .shareIn(scope, started, replay)

private fun SharedPreferences.changedKeyNames(): Flow<String> = callbackFlow {
  val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, name -> trySend(name) }
  registerOnSharedPreferenceChangeListener(listener)
  awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
}
