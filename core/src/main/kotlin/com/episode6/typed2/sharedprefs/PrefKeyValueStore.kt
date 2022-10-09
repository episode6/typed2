package com.episode6.typed2.sharedprefs

import com.episode6.typed2.*

interface PrefValueGetter : PrimitiveKeyValueGetter {
  fun getStringSet(name: String, defaultValue: Set<String>?): Set<String>?
}

interface PrefValueSetter : PrimitiveKeyValueSetter {
  fun setStringSet(name: String, value: Set<String>?)
}

fun <T> PrefValueGetter.get(key: PrefKey<T>): T = key.get(this)
fun <T> PrefValueSetter.set(key: PrefKey<T>, value: T) = key.set(this, value)
suspend fun <T> PrefValueGetter.get(key: AsyncPrefKey<T>): T = key.get(this)
suspend fun <T> PrefValueSetter.set(key: AsyncPrefKey<T>, value: T) = key.set(this, value)


