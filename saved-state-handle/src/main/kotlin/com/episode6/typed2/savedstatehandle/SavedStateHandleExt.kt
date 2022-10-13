package com.episode6.typed2.savedstatehandle

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.episode6.typed2.bundles.*

class TypedSavedStateHandle(private val delegate: SavedStateHandle) : BundleValueGetter, BundleValueSetter {
  override fun contains(name: String): Boolean = delegate.contains(name)
  override fun getBundle(name: String): Bundle? = delegate[name]
  override fun getInt(name: String, default: Int): Int = delegate[name] ?: default
  override fun getString(name: String, default: String?): String? = delegate[name]
  override fun setBundle(name: String, value: Bundle?) { delegate[name] = value }
  override fun setInt(name: String, value: Int) { delegate[name] = value }
  override fun setString(name: String, value: String?) { delegate[name] = value }
}


fun SavedStateHandle.typed(): TypedSavedStateHandle = TypedSavedStateHandle(this)

fun <T> SavedStateHandle.get(key: BundleKey<T>): T = typed().get(key)
fun <T> SavedStateHandle.set(key: BundleKey<T>, value: T) = typed().set(key, value)
suspend fun <T> SavedStateHandle.get(key: AsyncBundleKey<T>): T = typed().get(key)
suspend fun <T> SavedStateHandle.set(key: AsyncBundleKey<T>, value: T) = typed().set(key, value)
