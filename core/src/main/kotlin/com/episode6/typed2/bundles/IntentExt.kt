package com.episode6.typed2.bundles

import android.content.Intent
import android.os.Bundle

fun Intent.typedExtras(): TypedBundle = (extras ?: Bundle().also { putExtras(it) }).typed()

// used for get methods so we don't putExtras unnecessarily
private fun Intent.readOnlyTypedExtras(): TypedBundle = (extras ?: Bundle()).typed()

fun <T> Intent.getExtra(key: BundleKey<T, *>): T = readOnlyTypedExtras().get(key)
fun <T> Intent.setExtra(key: BundleKey<T, *>, value: T) = typedExtras().set(key, value)
fun Intent.removeExtra(key: BundleKey<*, *>) = typedExtras().remove(key)
suspend fun <T> Intent.getExtra(key: AsyncBundleKey<T, *>): T = readOnlyTypedExtras().get(key)
suspend fun <T> Intent.setExtra(key: AsyncBundleKey<T, *>, value: T) = typedExtras().set(key, value)
fun Intent.removeExtra(key: AsyncBundleKey<*, *>) = typedExtras().remove(key)

fun <T> Intent.extraProperty(key: BundleKey<T, *>): BundleProperty<T> = typedExtras().property(key)
