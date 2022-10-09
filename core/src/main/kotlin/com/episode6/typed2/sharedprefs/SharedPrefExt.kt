package com.episode6.typed2.sharedprefs

import android.content.SharedPreferences

class TypedSharedPreferences(private val delegate: SharedPreferences) : SharedPreferencesGetter {
  override fun contains(name: String): Boolean = delegate.contains(name)
  override fun getInt(name: String, default: Int): Int = delegate.getInt(name, default)
  override fun getString(name: String, default: String?): String? = delegate.getString(name, default)
  override fun getStringSet(name: String, defaultValue: Set<String>?): Set<String>? = delegate.getStringSet(name, defaultValue)?.toSet()

  class Editor(private val delegate: SharedPreferences.Editor) : SharedPreferencesSetter {
    override fun setString(name: String, value: String?) { delegate.putString(name, value) }
    override fun setInt(name: String, value: Int) { delegate.putInt(name, value) }
    override fun setStringSet(name: String, value: Set<String>?) { delegate.putStringSet(name, value) }
  }
}

fun SharedPreferences.typed(): TypedSharedPreferences = TypedSharedPreferences(this)
fun SharedPreferences.Editor.typed(): TypedSharedPreferences.Editor = TypedSharedPreferences.Editor(this)

fun <T> SharedPreferences.get(key: SharedPreferencesKey<T>): T = typed().get(key)
fun <T> SharedPreferences.Editor.set(key: SharedPreferencesKey<T>, value: T) = typed().set(key, value)
suspend fun <T> SharedPreferences.get(key: AsyncSharedPreferencesKey<T>): T = typed().get(key)
suspend fun <T> SharedPreferences.Editor.set(key: AsyncSharedPreferencesKey<T>, value: T) = typed().set(key, value)
