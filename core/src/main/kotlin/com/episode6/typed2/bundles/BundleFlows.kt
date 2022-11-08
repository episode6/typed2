package com.episode6.typed2.bundles

import android.os.Bundle
import com.episode6.typed2.DelegateAsyncMutableStateFlow
import com.episode6.typed2.DelegateMutableStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Duration

fun <T> Bundle.mutableStateFlow(
  key: BundleKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T> = typed().mutableStateFlow(key = key, scope = scope, debounceWrites = debounceWrites)

fun <T> TypedBundle.mutableStateFlow(
  key: BundleKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T> = DelegateMutableStateFlow(
  scope = scope,
  debounceWrites = debounceWrites,
  get = { get(key) },
  set = { set(key, it) },
)

fun <T> Bundle.mutableStateFlow(
  key: AsyncBundleKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T?> = typed().mutableStateFlow(key = key, scope = scope, debounceWrites = debounceWrites)

fun <T> TypedBundle.mutableStateFlow(
  key: AsyncBundleKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T?> = DelegateAsyncMutableStateFlow(
  scope = scope,
  debounceWrites = debounceWrites,
  get = { get(key) },
  set = { set(key, it) },
  remove = { remove(key) }
)
