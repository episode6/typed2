@file:OptIn(ExperimentalCoroutinesApi::class)

package com.episode6.typed2.sharedprefs

import android.content.SharedPreferences
import com.episode6.typed2.DelegateAsyncMutableStateFlow
import com.episode6.typed2.DelegateMutableStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.time.Duration

fun <T> SharedPreferences.flow(key: PrefKey<T, *>): Flow<T> = typed().flow(key)
fun <T> TypedSharedPreferences.flow(key: PrefKey<T, *>): Flow<T> =
  changedKeyNames()
    .filter { it == key.name }
    .mapLatest { get(key) }
    .onStart { emit(get(key)) }
    .distinctUntilChanged()

fun <T> SharedPreferences.flow(key: AsyncPrefKey<T, *>): Flow<T> = typed().flow(key)
fun <T> TypedSharedPreferences.flow(key: AsyncPrefKey<T, *>): Flow<T> =
  changedKeyNames()
    .filter { it == key.name }
    .mapLatest { get(key) }
    .onStart { emit(get(key)) }
    .distinctUntilChanged()


fun <T> SharedPreferences.mutableStateFlow(
  key: PrefKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T> = typed().mutableStateFlow(key = key, scope = scope, debounceWrites = debounceWrites)

fun <T> TypedSharedPreferences.mutableStateFlow(
  key: PrefKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T> = DelegateMutableStateFlow(
  scope = scope,
  debounceWrites = debounceWrites,
  get = { get(key) },
  set = { edit(true) { set(key, it) } },
  updates = flow(key)
)

fun <T> SharedPreferences.mutableStateFlow(
  key: AsyncPrefKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T?> = typed().mutableStateFlow(key = key, scope = scope, debounceWrites = debounceWrites)

fun <T> TypedSharedPreferences.mutableStateFlow(
  key: AsyncPrefKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T?> = DelegateAsyncMutableStateFlow(
  scope = scope,
  debounceWrites = debounceWrites,
  get = { get(key) },
  set = { edit(true) { set(key, it) } },
  remove = { edit(true) { remove(key) } },
  updates = flow(key)
)

