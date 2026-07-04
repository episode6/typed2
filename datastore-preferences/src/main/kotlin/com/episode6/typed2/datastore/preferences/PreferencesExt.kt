package com.episode6.typed2.datastore.preferences

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

public open class TypedPreferences(private val prefs: Preferences) : DataStoreValueGetter {
  override fun contains(name: String): Boolean = prefs.asMap().keys.any { it.name == name }
  override fun getBoolean(name: String, default: Boolean): Boolean = prefs[booleanPreferencesKey(name)] ?: default
  override fun getFloat(name: String, default: Float): Float = prefs[floatPreferencesKey(name)] ?: default
  override fun getInt(name: String, default: Int): Int = prefs[intPreferencesKey(name)] ?: default
  override fun getLong(name: String, default: Long): Long = prefs[longPreferencesKey(name)] ?: default
  override fun getString(name: String, default: String?): String? = prefs[stringPreferencesKey(name)] ?: default
  override fun getStringSet(name: String, defaultValue: Set<String?>?): Set<String?>? = prefs[stringSetPreferencesKey(name)] ?: defaultValue
}

public class TypedMutablePreferences(private val mutablePrefs: MutablePreferences) : TypedPreferences(mutablePrefs), DataStoreValueSetter {
  // Preferences.Key equality is name-based, so a string-typed key removes entries of any type
  override fun remove(name: String) { mutablePrefs.remove(stringPreferencesKey(name)) }
  override fun setBoolean(name: String, value: Boolean) { mutablePrefs[booleanPreferencesKey(name)] = value }
  override fun setFloat(name: String, value: Float) { mutablePrefs[floatPreferencesKey(name)] = value }
  override fun setInt(name: String, value: Int) { mutablePrefs[intPreferencesKey(name)] = value }
  override fun setLong(name: String, value: Long) { mutablePrefs[longPreferencesKey(name)] = value }

  // Preferences cannot store null values, so null writes remove the entry
  override fun setString(name: String, value: String?) {
    if (value == null) remove(name) else mutablePrefs[stringPreferencesKey(name)] = value
  }

  override fun setStringSet(name: String, value: Set<String?>?) {
    if (value == null) remove(name) else mutablePrefs[stringSetPreferencesKey(name)] = value.filterNotNull().toSet()
  }

  public fun clear() { mutablePrefs.clear() }
}

public fun Preferences.typed(): TypedPreferences = TypedPreferences(this)
public fun MutablePreferences.typed(): TypedMutablePreferences = TypedMutablePreferences(this)
