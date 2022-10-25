package com.episode6.typed2.sharedprefs

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TypedSharedPreferences(private val delegate: SharedPreferences) : PrefValueGetter {
  override fun contains(name: String): Boolean = delegate.contains(name)
  override fun getInt(name: String, default: Int): Int = delegate.getInt(name, default)
  override fun getString(name: String, default: String?): String? = delegate.getString(name, default)
  override fun getStringSet(name: String, defaultValue: Set<String?>?): Set<String?>? = delegate.getStringSet(name, defaultValue)?.toSet()
  fun edit(): Editor = Editor(delegate.edit())

  class Editor(private val delegate: SharedPreferences.Editor) : PrefValueSetter {
    override fun remove(name: String) { delegate.remove(name) }
    override fun setString(name: String, value: String?) { delegate.putString(name, value) }
    override fun setInt(name: String, value: Int) { delegate.putInt(name, value) }
    override fun setStringSet(name: String, value: Set<String?>?) { delegate.putStringSet(name, value) }
    fun commit() { delegate.commit() }
    fun apply() { delegate.apply() }
  }
}

fun SharedPreferences.typed(): TypedSharedPreferences = TypedSharedPreferences(this)
fun SharedPreferences.Editor.typed(): TypedSharedPreferences.Editor = TypedSharedPreferences.Editor(this)

fun <T> SharedPreferences.get(key: PrefKey<T, *>): T = typed().get(key)
fun <T> SharedPreferences.Editor.set(key: PrefKey<T, *>, value: T) = typed().set(key, value)
fun SharedPreferences.Editor.remove(key: PrefKey<*, *>) = typed().remove(key)
suspend fun <T> SharedPreferences.get(key: AsyncPrefKey<T, *>): T = typed().get(key)
suspend fun <T> SharedPreferences.Editor.set(key: AsyncPrefKey<T, *>, value: T) = typed().set(key, value)
fun SharedPreferences.Editor.remove(key: AsyncPrefKey<*, *>) = typed().remove(key)

inline fun SharedPreferences.edit(
  commit: Boolean = false,
  action: TypedSharedPreferences.Editor.() -> Unit
) = typed().edit(commit = commit, action = action)

inline fun TypedSharedPreferences.edit(
  commit: Boolean = false,
  action: TypedSharedPreferences.Editor.() -> Unit
) {
  val editor = edit()
  editor.action()
  when {
    commit -> editor.commit()
    else   -> editor.apply()
  }
}

inline fun SharedPreferences.launchEdit(
  scope: CoroutineScope,
  commit: Boolean = false,
  crossinline action: suspend TypedSharedPreferences.Editor.() -> Unit
) = typed().launchEdit(scope = scope, commit = commit, action = action)

inline fun TypedSharedPreferences.launchEdit(
  scope: CoroutineScope,
  commit: Boolean = false,
  crossinline action: suspend TypedSharedPreferences.Editor.() -> Unit
) {
  val editor = edit()
  scope.launch {
    editor.action()
    when {
      commit -> editor.commit()
      else   -> editor.apply()
    }
  }
}
