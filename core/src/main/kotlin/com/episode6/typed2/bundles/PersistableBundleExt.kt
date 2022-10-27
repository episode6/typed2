package com.episode6.typed2.bundles

import android.os.PersistableBundle
import com.episode6.typed2.KeyValueDelegate

class TypedPersistableBundle(private val delegate: PersistableBundle) : PersistableBundleValueGetter, PersistableBundleValueSetter {
  override fun getPersistableBundle(name: String): PersistableBundle? = delegate.getPersistableBundle(name)
  override fun contains(name: String): Boolean = delegate.containsKey(name)
  override fun getInt(name: String, default: Int): Int = delegate.getInt(name, default)
  override fun getString(name: String, default: String?): String? = delegate.getString(name, default)
  override fun getDouble(name: String, default: Double): Double = delegate.getDouble(name, default)

  override fun setPersistableBundle(name: String, value: PersistableBundle?) { delegate.putPersistableBundle(name, value) }
  override fun remove(name: String) = delegate.remove(name)
  override fun setString(name: String, value: String?) = delegate.putString(name, value)
  override fun setInt(name: String, value: Int) = delegate.putInt(name, value)
  override fun setDouble(name: String, value: Double) { delegate.putDouble(name, value) }
}

fun PersistableBundle.typed(): TypedPersistableBundle = TypedPersistableBundle(this)

fun <T> PersistableBundle.get(key: PersistableBundleKey<T, *>): T = typed().get(key)
fun <T> PersistableBundle.set(key: PersistableBundleKey<T, *>, value: T) = typed().set(key, value)
fun PersistableBundle.remove(key: PersistableBundleKey<*, *>) = typed().remove(key)
suspend fun <T> PersistableBundle.get(key: AsyncPersistableBundleKey<T, *>): T = typed().get(key)
suspend fun <T> PersistableBundle.set(key: AsyncPersistableBundleKey<T, *>, value: T) = typed().set(key, value)
fun PersistableBundle.remove(key: AsyncPersistableBundleKey<*, *>) = typed().remove(key)

fun <T> TypedPersistableBundle.property(key: PersistableBundleKey<T, *>): PersistableBundleProperty<T> = KeyValueDelegate(key, { this }, { this })
fun <T> PersistableBundle.property(key: PersistableBundleKey<T, *>): PersistableBundleProperty<T> = typed().property(key)
