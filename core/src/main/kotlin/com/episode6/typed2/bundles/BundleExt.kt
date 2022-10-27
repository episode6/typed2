package com.episode6.typed2.bundles

import android.os.Bundle
import com.episode6.typed2.KeyValueDelegate

class TypedBundle(private val delegate: Bundle) : BundleValueGetter, BundleValueSetter {
  override fun contains(name: String): Boolean = delegate.containsKey(name)
  override fun getBoolean(name: String, default: Boolean): Boolean = delegate.getBoolean(name, default)
  override fun getFloat(name: String, default: Float): Float = delegate.getFloat(name, default)
  override fun getInt(name: String, default: Int): Int = delegate.getInt(name, default)
  override fun getLong(name: String, default: Long): Long = delegate.getLong(name, default)
  override fun getString(name: String, default: String?): String? = delegate.getString(name, default)
  override fun getBundle(name: String): Bundle? = delegate.getBundle(name)
  override fun getDouble(name: String, default: Double): Double = delegate.getDouble(name, default)

  override fun remove(name: String) = delegate.remove(name)
  override fun setString(name: String, value: String?) = delegate.putString(name, value)
  override fun setInt(name: String, value: Int) = delegate.putInt(name, value)
  override fun setBoolean(name: String, value: Boolean) { delegate.putBoolean(name, value) }
  override fun setFloat(name: String, value: Float) { delegate.putFloat(name, value) }
  override fun setLong(name: String, value: Long) { delegate.putLong(name, value) }
  override fun setBundle(name: String, value: Bundle?) = delegate.putBundle(name, value)
  override fun setDouble(name: String, value: Double) { delegate.putDouble(name, value) }
}

fun Bundle.typed(): TypedBundle = TypedBundle(this)

fun <T> Bundle.get(key: BundleKey<T, *>): T = typed().get(key)
fun <T> Bundle.set(key: BundleKey<T, *>, value: T) = typed().set(key, value)
fun Bundle.remove(key: BundleKey<*, *>) = typed().remove(key)
suspend fun <T> Bundle.get(key: AsyncBundleKey<T, *>): T = typed().get(key)
suspend fun <T> Bundle.set(key: AsyncBundleKey<T, *>, value: T) = typed().set(key, value)
fun Bundle.remove(key: AsyncBundleKey<*, *>) = typed().remove(key)

fun <T> TypedBundle.property(key: BundleKey<T, *>): BundleProperty<T> = KeyValueDelegate(key, { this }, { this })
fun <T> Bundle.property(key: BundleKey<T, *>): BundleProperty<T> = typed().property(key)
