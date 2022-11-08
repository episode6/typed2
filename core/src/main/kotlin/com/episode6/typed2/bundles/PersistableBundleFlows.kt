package com.episode6.typed2.bundles

import android.os.PersistableBundle
import com.episode6.typed2.DelegateAsyncMutableStateFlow
import com.episode6.typed2.DelegateMutableStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Duration

fun <T> PersistableBundle.mutableStateFlow(
  key: PersistableBundleKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T> = typed().mutableStateFlow(key = key, scope = scope, debounceWrites = debounceWrites)

fun <T> TypedPersistableBundle.mutableStateFlow(
  key: PersistableBundleKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T> = DelegateMutableStateFlow(
  scope = scope,
  debounceWrites = debounceWrites,
  get = { get(key) },
  set = { set(key, it) },
)

fun <T> PersistableBundle.mutableStateFlow(
  key: AsyncPersistableBundleKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T?> = typed().mutableStateFlow(key = key, scope = scope, debounceWrites = debounceWrites)

fun <T> TypedPersistableBundle.mutableStateFlow(
  key: AsyncPersistableBundleKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T?> = DelegateAsyncMutableStateFlow(
  scope = scope,
  debounceWrites = debounceWrites,
  get = { get(key) },
  set = { set(key, it) },
  remove = { remove(key) }
)
