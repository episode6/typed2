package com.episode6.typed2

interface PrimitiveKeyValueGetter : KeyValueGetter {
  fun getInt(name: String, default: Int): Int
  fun getString(name: String, default: String?): String?
}

interface PrimitiveKeyValueSetter : KeyValueSetter {
  fun setInt(name: String, value: Int)
  fun setString(name: String, value: String?)
}

fun <T, BACKED_BY> PrimitiveKeyValueGetter.get(key: PrimitiveKey<T, BACKED_BY>): T = key.get(this)
fun <T, BACKED_BY> PrimitiveKeyValueSetter.set(key: PrimitiveKey<T, BACKED_BY>, value: T) = key.set(this, value)
fun <T, BACKED_BY> PrimitiveKeyValueSetter.remove(key: PrimitiveKey<T, BACKED_BY>) = remove(key.name)
suspend fun <T, BACKED_BY> PrimitiveKeyValueGetter.get(key: AsyncPrimitiveKey<T, BACKED_BY>): T = key.get(this)
suspend fun <T, BACKED_BY> PrimitiveKeyValueSetter.set(key: AsyncPrimitiveKey<T, BACKED_BY>, value: T) = key.set(this, value)
fun <T, BACKED_BY> PrimitiveKeyValueSetter.remove(key: AsyncPrimitiveKey<T, BACKED_BY>) = remove(key.name)
