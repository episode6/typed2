package com.episode6.typed2.bundles

import android.os.Bundle
import com.episode6.typed2.PrimitiveKeyValueGetter
import com.episode6.typed2.PrimitiveKeyValueSetter

interface BundleValueGetter : PrimitiveKeyValueGetter {
  fun getBundle(name: String): Bundle?
}
interface BundleValueSetter : PrimitiveKeyValueSetter {
  fun setBundle(name: String, value: Bundle?)
}

fun <T> BundleValueGetter.get(key: BundleKey<T>): T = key.get(this)
fun <T> BundleValueSetter.set(key: BundleKey<T>, value: T) = key.set(this, value)
suspend fun <T> BundleValueGetter.get(key: AsyncBundleKey<T>): T = key.get(this)
suspend fun <T> BundleValueSetter.set(key: AsyncBundleKey<T>, value: T) = key.set(this, value)
