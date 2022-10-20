package com.episode6.typed2.bundles

import android.os.Bundle
import com.episode6.typed2.*

interface BundleValueGetter : PrimitiveKeyValueGetter {
  fun getBundle(name: String): Bundle?
}
interface BundleValueSetter : PrimitiveKeyValueSetter {
  fun setBundle(name: String, value: Bundle?)
}

fun <T, BACKED_BY> BundleValueGetter.get(key: BundleKey<T, BACKED_BY>): T = key.get(this)
fun <T, BACKED_BY> BundleValueSetter.set(key: BundleKey<T, BACKED_BY>, value: T) = key.set(this, value)
fun <T, BACKED_BY> BundleValueSetter.remove(key: BundleKey<T, BACKED_BY>) = remove(key.name)
suspend fun <T, BACKED_BY> BundleValueGetter.get(key: AsyncBundleKey<T, BACKED_BY>): T = key.get(this)
suspend fun <T, BACKED_BY> BundleValueSetter.set(key: AsyncBundleKey<T, BACKED_BY>, value: T) = key.set(this, value)
fun <T, BACKED_BY> BundleValueSetter.remove(key: AsyncBundleKey<T, BACKED_BY>) = remove(key.name)
