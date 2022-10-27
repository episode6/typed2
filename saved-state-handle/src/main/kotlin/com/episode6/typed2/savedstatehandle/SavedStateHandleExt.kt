package com.episode6.typed2.savedstatehandle

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.episode6.typed2.KeyValueDelegate
import com.episode6.typed2.bundles.*

class TypedSavedStateHandle(private val delegate: SavedStateHandle) : BundleValueGetter, BundleValueSetter {
  override fun contains(name: String): Boolean = delegate.contains(name)
  override fun getBundle(name: String): Bundle? = delegate[name]
  override fun getDouble(name: String, default: Double): Double = delegate[name] ?: default
  override fun getBoolean(name: String, default: Boolean): Boolean = delegate[name] ?: default
  override fun getFloat(name: String, default: Float): Float = delegate[name] ?: default
  override fun getLong(name: String, default: Long): Long = delegate[name] ?: default
  override fun getInt(name: String, default: Int): Int = delegate[name] ?: default
  override fun getString(name: String, default: String?): String? = delegate[name] ?: default
  override fun remove(name: String) { delegate.remove<Any>(name) }
  override fun setBundle(name: String, value: Bundle?) { delegate[name] = value }
  override fun setDouble(name: String, value: Double) { delegate[name] = value }
  override fun setInt(name: String, value: Int) { delegate[name] = value }
  override fun setString(name: String, value: String?) { delegate[name] = value }
  override fun setBoolean(name: String, value: Boolean) { delegate[name] = value }
  override fun setFloat(name: String, value: Float) { delegate[name] = value }
  override fun setLong(name: String, value: Long) { delegate[name] = value }
}

fun SavedStateHandle.typed(): TypedSavedStateHandle = TypedSavedStateHandle(this)

fun <T> SavedStateHandle.get(key: BundleKey<T, *>): T = typed().get(key)
fun <T> SavedStateHandle.set(key: BundleKey<T, *>, value: T) = typed().set(key, value)
fun SavedStateHandle.remove(key: BundleKey<*,*>) { remove<Any>(key.name) }
suspend fun <T> SavedStateHandle.get(key: AsyncBundleKey<T, *>): T = typed().get(key)
suspend fun <T> SavedStateHandle.set(key: AsyncBundleKey<T, *>, value: T) = typed().set(key, value)
fun SavedStateHandle.remove(key: AsyncBundleKey<*, *>) { remove<Any>(key.name) }

fun <T> TypedSavedStateHandle.property(key: BundleKey<T, *>): BundleProperty<T> = KeyValueDelegate(key, { this }, { this })
fun <T> SavedStateHandle.property(key: BundleKey<T, *>): BundleProperty<T> = typed().property(key)
