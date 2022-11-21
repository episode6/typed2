package com.episode6.typed2.bundles

import android.content.Intent
import android.os.Bundle
import com.episode6.typed2.DelegateProperty
import kotlinx.coroutines.CoroutineScope


fun <T> Intent.getExtra(key: BundleKey<T, *>): T = (extras ?: Bundle()).typed().get(key)
fun <T> Intent.setExtra(key: BundleKey<T, *>, value: T) = putExtras(Bundle().apply { set(key, value) })
fun Intent.removeExtra(key: BundleKey<*, *>) = removeExtra(key.name)
suspend fun <T> Intent.getExtra(key: AsyncBundleKey<T, *>): T = (extras ?: Bundle()).typed().get(key)
suspend fun <T> Intent.setExtra(key: AsyncBundleKey<T, *>, value: T) = putExtras(Bundle().apply { set(key, value) })
fun Intent.removeExtra(key: AsyncBundleKey<*, *>) = removeExtra(key.name)

fun <T> Intent.extraProperty(key: BundleKey<T, *>): DelegateProperty<T> = DelegateProperty(
  get = { getExtra(key) },
  set = { setExtra(key, it) }
)
fun <T> Intent.extraProperty(key: AsyncBundleKey<T, *>, scope: CoroutineScope): DelegateProperty<T?> =
  DelegateProperty(mutableStateFlow(key, scope))

inline fun <T> Intent.updateExtra(key: BundleKey<T, *>, reducer: (T)->T) {
  setExtra(key, reducer(getExtra(key)))
}

suspend inline fun <T> Intent.updateExtra(key: AsyncBundleKey<T, *>, reducer: (T)->T) {
  setExtra(key, reducer(getExtra(key)))
}
