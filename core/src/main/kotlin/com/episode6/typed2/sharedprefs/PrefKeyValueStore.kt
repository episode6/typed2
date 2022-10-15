package com.episode6.typed2.sharedprefs

import com.episode6.typed2.*

interface PrefValueGetter : PrimitiveKeyValueGetter {
  fun getStringSet(name: String, defaultValue: Set<String?>?): Set<String?>?
}

interface PrefValueSetter : PrimitiveKeyValueSetter {
  fun setStringSet(name: String, value: Set<String?>?)
}

fun <T, BACKED_BY> PrefValueGetter.get(key: PrefKey<T, BACKED_BY>): T = key.get(this)
fun <T, BACKED_BY> PrefValueSetter.set(key: PrefKey<T, BACKED_BY>, value: T) = key.set(this, value)
suspend fun <T, BACKED_BY> PrefValueGetter.get(key: AsyncPrefKey<T, BACKED_BY>): T = key.get(this)
suspend fun <T, BACKED_BY> PrefValueSetter.set(key: AsyncPrefKey<T, BACKED_BY>, value: T) = key.set(this, value)


