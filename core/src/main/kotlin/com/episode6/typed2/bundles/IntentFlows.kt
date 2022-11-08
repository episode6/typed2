package com.episode6.typed2.bundles

import android.content.Intent
import com.episode6.typed2.DelegateAsyncMutableStateFlow
import com.episode6.typed2.DelegateMutableStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Duration

fun <T> Intent.mutableStateFlow(
  key: BundleKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T> = DelegateMutableStateFlow(
  scope = scope,
  debounceWrites = debounceWrites,
  get = { getExtra(key) },
  set = { setExtra(key, it) },
)

fun <T> Intent.mutableStateFlow(
  key: AsyncBundleKey<T, *>,
  scope: CoroutineScope,
  debounceWrites: Duration = Duration.ZERO,
): MutableStateFlow<T?> = DelegateAsyncMutableStateFlow(
  scope = scope,
  debounceWrites = debounceWrites,
  get = { getExtra(key) },
  set = { setExtra(key, it) },
  remove = { removeExtra(key) }
)
