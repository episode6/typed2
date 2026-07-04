package com.episode6.typed2.datastore.preferences

import com.episode6.typed2.*

public interface DataStoreValueGetter : PrimitiveKeyValueGetter {
  public fun getStringSet(name: String, defaultValue: Set<String?>?): Set<String?>?
}

public interface DataStoreValueSetter : PrimitiveKeyValueSetter {
  public fun setStringSet(name: String, value: Set<String?>?)
}

public suspend fun <T> DataStoreValueGetter.get(key: DataStoreKey<T, *>): T = key.get(this)
public suspend fun <T> DataStoreValueSetter.set(key: DataStoreKey<T, *>, value: T): Unit = key.set(this, value)
public fun DataStoreValueSetter.remove(key: DataStoreKey<*, *>): Unit = remove(key.name)
