package com.episode6.typed2.bundles

import android.content.Intent
import android.os.Bundle
import com.episode6.typed2.DelegateProperty


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

