package com.episode6.typed2.sharedprefs

import android.content.SharedPreferences

class TypedSharedPreferences(private val delegate: SharedPreferences) : PrefValueGetter {
  override fun contains(name: String): Boolean = delegate.contains(name)
  override fun getInt(name: String, default: Int): Int = delegate.getInt(name, default)
  override fun getString(name: String, default: String?): String? = delegate.getString(name, default)
  override fun getStringSet(name: String, defaultValue: Set<String?>?): Set<String?>? = delegate.getStringSet(name, defaultValue)?.toSet()

  class Editor(private val delegate: SharedPreferences.Editor) : PrefValueSetter {
    override fun remove(name: String) { delegate.remove(name) }
    override fun setString(name: String, value: String?) { delegate.putString(name, value) }
    override fun setInt(name: String, value: Int) { delegate.putInt(name, value) }
    override fun setStringSet(name: String, value: Set<String?>?) { delegate.putStringSet(name, value) }
  }
}

fun SharedPreferences.typed(): TypedSharedPreferences = TypedSharedPreferences(this)
fun SharedPreferences.Editor.typed(): TypedSharedPreferences.Editor = TypedSharedPreferences.Editor(this)

fun <T, BACKED_BY> SharedPreferences.get(key: PrefKey<T, BACKED_BY>): T = typed().get(key)
fun <T, BACKED_BY> SharedPreferences.Editor.set(key: PrefKey<T, BACKED_BY>, value: T) = typed().set(key, value)
fun <T, BACKED_BY> SharedPreferences.Editor.remove(key: PrefKey<T, BACKED_BY>) = typed().remove(key)
suspend fun <T, BACKED_BY> SharedPreferences.get(key: AsyncPrefKey<T, BACKED_BY>): T = typed().get(key)
suspend fun <T, BACKED_BY> SharedPreferences.Editor.set(key: AsyncPrefKey<T, BACKED_BY>, value: T) = typed().set(key, value)
fun <T, BACKED_BY> SharedPreferences.Editor.remove(key: AsyncPrefKey<T, BACKED_BY>) = typed().remove(key)
