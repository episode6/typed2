package com.episode6.typed2.plugins

import android.content.SharedPreferences
import com.episode6.typed2.*

class TypedSharePreferences(private val delegate: SharedPreferences): PrimitiveKeyValueGetter {
  override fun contains(name: String): Boolean = delegate.contains(name)
  override fun getInt(name: String, default: Int): Int = delegate.getInt(name, default)
  override fun getString(name: String, default: String?): String? = delegate.getString(name, default)

  class Editor(private val delegate: SharedPreferences.Editor) : PrimitiveKeyValueSetter {
    override fun setString(name: String, value: String?) { delegate.putString(name, value) }
    override fun setInt(name: String, value: Int) { delegate.putInt(name, value) }
  }
}

interface SharedPreferencesKeyBuilder : PrimitiveKeyBuilder
private class SharedPreferencesKeyBuilderImpl(override val name: String) : SharedPreferencesKeyBuilder
open class SharedPreferencesNamespace(private val prefix: String = "") {
  protected fun key(name: String): SharedPreferencesKeyBuilder = SharedPreferencesKeyBuilderImpl(prefix + name)
}

fun SharedPreferences.typed(): TypedSharePreferences = TypedSharePreferences(this)
fun SharedPreferences.Editor.typed(): TypedSharePreferences.Editor = TypedSharePreferences.Editor(this)

fun <T> SharedPreferences.get(key: PrimitiveKey<T>): T = typed().get(key)
fun <T> SharedPreferences.Editor.set(key: PrimitiveKey<T>, value: T) = typed().set(key, value)
suspend fun <T> SharedPreferences.get(key: AsyncPrimitiveKey<T>): T = typed().get(key)
suspend fun <T> SharedPreferences.Editor.set(key: AsyncPrimitiveKey<T>, value: T) = typed().set(key, value)
