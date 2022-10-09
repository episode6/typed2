package com.episode6.typed2.sharedprefs

import com.episode6.typed2.*

interface SharedPreferencesGetter : PrimitiveKeyValueGetter {
  fun getStringSet(name: String, defaultValue: Set<String>?): Set<String>?
}

interface SharedPreferencesSetter : PrimitiveKeyValueSetter {
  fun setStringSet(name: String, value: Set<String>?)
}

fun <T> SharedPreferencesGetter.get(key: SharedPreferencesKey<T>): T = key.get(this)
fun <T> SharedPreferencesSetter.set(key: SharedPreferencesKey<T>, value: T) = key.set(this, value)
suspend fun <T> SharedPreferencesGetter.get(key: AsyncSharedPreferencesKey<T>): T = key.get(this)
suspend fun <T> SharedPreferencesSetter.set(key: AsyncSharedPreferencesKey<T>, value: T) = key.set(this, value)


