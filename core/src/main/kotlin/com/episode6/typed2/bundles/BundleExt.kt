package com.episode6.typed2.bundles

import android.os.Bundle
import com.episode6.typed2.*

class TypedBundle(private val delegate: Bundle) : BundleValueGetter, BundleValueSetter {
  override fun contains(name: String): Boolean = delegate.containsKey(name)
  override fun getInt(name: String, default: Int): Int = delegate.getInt(name, default)
  override fun getString(name: String, default: String?): String? = delegate.getString(name, default)
  override fun getBundle(name: String): Bundle? = delegate.getBundle(name)

  override fun setString(name: String, value: String?) = delegate.putString(name, value)
  override fun setInt(name: String, value: Int) = delegate.putInt(name, value)
  override fun setBundle(name: String, value: Bundle?) = delegate.putBundle(name, value)
}

fun Bundle.typed(): TypedBundle = TypedBundle(this)

fun <T, BACKED_BY> Bundle.get(key: BundleKey<T, BACKED_BY>): T = typed().get(key)
fun <T, BACKED_BY> Bundle.set(key: BundleKey<T, BACKED_BY>, value: T) = typed().set(key, value)
suspend fun <T, BACKED_BY> Bundle.get(key: AsyncBundleKey<T, BACKED_BY>): T = typed().get(key)
suspend fun <T, BACKED_BY> Bundle.set(key: AsyncBundleKey<T, BACKED_BY>, value: T) = typed().set(key, value)
