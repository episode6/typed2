package com.episode6.typed2.sharedprefs

import android.content.SharedPreferences
import androidx.core.content.edit
import com.episode6.typed2.DelegateProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class TypedSharedPreferences(private val delegate: SharedPreferences) : PrefValueGetter {
  override fun contains(name: String): Boolean = delegate.contains(name)
  override fun getBoolean(name: String, default: Boolean): Boolean = delegate.getBoolean(name, default)
  override fun getFloat(name: String, default: Float): Float = delegate.getFloat(name, default)
  override fun getInt(name: String, default: Int): Int = delegate.getInt(name, default)
  override fun getLong(name: String, default: Long): Long = delegate.getLong(name, default)
  override fun getString(name: String, default: String?): String? = delegate.getString(name, default)
  override fun getStringSet(name: String, defaultValue: Set<String?>?): Set<String?>? = delegate.getStringSet(name, defaultValue)?.toSet()

  fun edit(): Editor = Editor(delegate.edit())
  fun changedKeyNames(): Flow<String> = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, name -> trySend(name) }
    delegate.registerOnSharedPreferenceChangeListener(listener)
    awaitClose { delegate.unregisterOnSharedPreferenceChangeListener(listener) }
  }

  class Editor(private val delegate: SharedPreferences.Editor) : PrefValueSetter {
    override fun remove(name: String) { delegate.remove(name) }
    override fun setString(name: String, value: String?) { delegate.putString(name, value) }
    override fun setInt(name: String, value: Int) { delegate.putInt(name, value) }
    override fun setBoolean(name: String, value: Boolean) { delegate.putBoolean(name, value) }
    override fun setFloat(name: String, value: Float) { delegate.putFloat(name, value) }
    override fun setLong(name: String, value: Long) { delegate.putLong(name, value) }
    override fun setStringSet(name: String, value: Set<String?>?) { delegate.putStringSet(name, value) }
    fun clear() { delegate.clear() }
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

fun <T> TypedSharedPreferences.property(key: PrefKey<T, *>): DelegateProperty<T> = DelegateProperty<T>(
  get = { get(key) },
  set = { edit { set(key, it) } }
)
fun <T> SharedPreferences.property(key: PrefKey<T, *>): DelegateProperty<T> = typed().property(key)

fun <T> TypedSharedPreferences.property(key: AsyncPrefKey<T, *>, scope: CoroutineScope): DelegateProperty<T?> = DelegateProperty(mutableStateFlow(key, scope))
fun <T> SharedPreferences.property(key: AsyncPrefKey<T, *>, scope: CoroutineScope): DelegateProperty<T?> = typed().property(key, scope)

inline fun TypedSharedPreferences.edit(
  commit: Boolean = false,
  action: TypedSharedPreferences.Editor.() -> Unit,
) {
  edit().apply {
    action()
    if (commit) commit() else apply()
  }
}

fun SharedPreferences.launchEdit(
  scope: CoroutineScope,
  commit: Boolean = false,
  action: suspend SharedPreferences.Editor.() -> Unit,
): Job = scope.launch { edit(commit = commit) { action() } }

fun TypedSharedPreferences.launchEdit(
  scope: CoroutineScope,
  commit: Boolean = false,
  action: suspend TypedSharedPreferences.Editor.() -> Unit,
): Job = scope.launch { edit(commit = commit) { action() } }


