package com.episode6.typed2.sharedprefs

import com.episode6.typed2.*

public interface PrefValueGetter : PrimitiveKeyValueGetter {
  public fun getStringSet(name: String, defaultValue: Set<String?>?): Set<String?>?
}

public interface PrefValueSetter : PrimitiveKeyValueSetter {
  public fun setStringSet(name: String, value: Set<String?>?)
}

public fun <T> PrefValueGetter.get(key: PrefKey<T, *>): T = key.get(this)
public fun <T> PrefValueSetter.set(key: PrefKey<T, *>, value: T) = key.set(this, value)
public fun PrefValueSetter.remove(key: PrefKey<*, *>) = remove(key.name)
public suspend fun <T> PrefValueGetter.get(key: AsyncPrefKey<T, *>): T = key.get(this)
public suspend fun <T> PrefValueSetter.set(key: AsyncPrefKey<T, *>, value: T) = key.set(this, value)
public fun PrefValueSetter.remove(key: AsyncPrefKey<*, *>) = remove(key.name)


