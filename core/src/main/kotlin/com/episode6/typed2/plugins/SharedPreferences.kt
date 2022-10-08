package com.episode6.typed2.plugins

import android.content.SharedPreferences
import com.episode6.typed2.*

class TypedSharePreferences(private val delegate: SharedPreferences): ObnoxiousKeyValueGetter {
  override fun contains(name: String): Boolean = delegate.contains(name)
  override fun getInt(name: String, default: Int): Int = delegate.getInt(name, default)
  override fun getString(name: String, default: String?): String? = delegate.getString(name, default)

  class Editor(private val delegate: SharedPreferences.Editor) : ObnoxiousKeyValueSetter {
    override fun setString(name: String, value: String?) { delegate.putString(name, value) }
    override fun setInt(name: String, value: Int) { delegate.putInt(name, value) }
  }
}

fun SharedPreferences.typed(): TypedSharePreferences = TypedSharePreferences(this)
fun SharedPreferences.Editor.typed(): TypedSharePreferences.Editor = TypedSharePreferences.Editor(this)

fun <RAW, T> SharedPreferences.get(key: Key<RAW, T>): T = typed().get(key)
suspend fun <RAW, T> SharedPreferences.get(key: AsyncKey<RAW, T>): T = typed().get(key)

fun <RAW, T> SharedPreferences.Editor.set(key: Key<RAW, T>, value: T) = typed().set(key, value)
suspend fun <RAW, T> SharedPreferences.Editor.set(key: AsyncKey<RAW, T>, value: T) = typed().set(key, value)
