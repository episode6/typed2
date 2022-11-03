package com.episode6.typed2.bundles

import android.content.Intent
import android.os.Bundle
import com.episode6.typed2.*
import com.episode6.typed2.KeyValueDelegate
import kotlin.reflect.KProperty


fun <T> Intent.getExtra(key: BundleKey<T, *>): T = (extras ?: Bundle()).typed().get(key)
fun <T> Intent.setExtra(key: BundleKey<T, *>, value: T) = putExtras(Bundle().apply { set(key, value) })
fun Intent.removeExtra(key: BundleKey<*, *>) = removeExtra(key.name)
suspend fun <T> Intent.getExtra(key: AsyncBundleKey<T, *>): T = (extras ?: Bundle()).typed().get(key)
suspend fun <T> Intent.setExtra(key: AsyncBundleKey<T, *>, value: T) = putExtras(Bundle().apply { set(key, value) })
fun Intent.removeExtra(key: AsyncBundleKey<*, *>) = removeExtra(key.name)

fun <T> Intent.extraProperty(key: BundleKey<T, *>): IntentKeyValueDelegate<T> = IntentKeyValueDelegate(key, this)

class IntentKeyValueDelegate<T : Any?>(
  private val key: BundleKey<T, *>,
  private val intent: Intent,
) {
  operator fun getValue(thisRef: Any?, property: KProperty<*>): T = intent.getExtra(key)
  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = intent.setExtra(key, value)
}
