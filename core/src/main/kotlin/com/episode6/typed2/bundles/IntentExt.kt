package com.episode6.typed2.bundles

import android.content.Intent
import android.os.Bundle
import com.episode6.typed2.DelegateProperty
import kotlinx.coroutines.CoroutineScope


public fun <T> Intent.getExtra(key: BundleKey<T, *>): T = (extras ?: Bundle()).typed().get(key)
public fun <T> Intent.setExtra(key: BundleKey<T, *>, value: T): Unit = putExtras(Bundle().apply { set(key, value) })
public fun Intent.removeExtra(key: BundleKey<*, *>): Unit = removeExtra(key.name)
public suspend fun <T> Intent.getExtra(key: AsyncBundleKey<T, *>): T = (extras ?: Bundle()).typed().get(key)
public suspend fun <T> Intent.setExtra(key: AsyncBundleKey<T, *>, value: T): Unit = putExtras(Bundle().apply { set(key, value) })
public fun Intent.removeExtra(key: AsyncBundleKey<*, *>): Unit = removeExtra(key.name)

public fun <T> Intent.extraProperty(key: BundleKey<T, *>): DelegateProperty<T> = DelegateProperty(
  get = { getExtra(key) },
  set = { setExtra(key, it) }
)
public fun <T> Intent.extraProperty(key: AsyncBundleKey<T, *>, scope: CoroutineScope): DelegateProperty<T?> =
  DelegateProperty(mutableStateFlow(key, scope))

public inline fun <T> Intent.updateExtra(key: BundleKey<T, *>, reducer: (T)->T) {
  setExtra(key, reducer(getExtra(key)))
}

public suspend inline fun <T> Intent.updateExtra(key: AsyncBundleKey<T, *>, reducer: (T)->T) {
  setExtra(key, reducer(getExtra(key)))
}
