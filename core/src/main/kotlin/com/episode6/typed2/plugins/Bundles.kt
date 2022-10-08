package com.episode6.typed2.plugins

import android.os.Bundle
import com.episode6.typed2.*


class TypedBundle(private val delegate: Bundle) : ObnoxiousKeyValueGetter, ObnoxiousKeyValueSetter {
  override fun contains(name: String): Boolean = delegate.containsKey(name)
  override fun getInt(name: String, default: Int): Int = delegate.getInt(name, default)
  override fun getString(name: String, default: String?): String? = delegate.getString(name, default)

  override fun setString(name: String, value: String?) = delegate.putString(name, value)
  override fun setInt(name: String, value: Int) = delegate.putInt(name, value)
}

fun Bundle.typed(): TypedBundle = TypedBundle(this)

fun <RAW, T> Bundle.get(key: Key<RAW, T>): T = typed().get(key)
suspend fun <RAW, T> Bundle.get(key: AsyncKey<RAW, T>): T = typed().get(key)

fun <RAW, T> Bundle.set(key: Key<RAW, T>, value: T) = typed().set(key, value)
suspend fun <RAW, T> Bundle.set(key: AsyncKey<RAW, T>, value: T) = typed().set(key, value)
