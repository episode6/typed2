package com.episode6.typed2

interface PrimitiveKeyValueGetter : KeyValueGetter {
  fun getBoolean(name: String, default: Boolean): Boolean
  fun getFloat(name: String, default: Float): Float
  fun getInt(name: String, default: Int): Int
  fun getLong(name: String, default: Long): Long
  fun getString(name: String, default: String?): String?
}

interface PrimitiveKeyValueSetter : KeyValueSetter {
  fun setBoolean(name: String, value: Boolean)
  fun setFloat(name: String, value: Float)
  fun setInt(name: String, value: Int)
  fun setLong(name: String, value: Long)
  fun setString(name: String, value: String?)
}

fun <T> PrimitiveKeyValueGetter.get(key: PrimitiveKey<T, *>): T = key.get(this)
fun <T> PrimitiveKeyValueSetter.set(key: PrimitiveKey<T, *>, value: T) = key.set(this, value)
fun PrimitiveKeyValueSetter.remove(key: PrimitiveKey<*, *>) = remove(key.name)
suspend fun <T> PrimitiveKeyValueGetter.get(key: AsyncPrimitiveKey<T, *>): T = key.get(this)
suspend fun <T> PrimitiveKeyValueSetter.set(key: AsyncPrimitiveKey<T, *>, value: T) = key.set(this, value)
fun PrimitiveKeyValueSetter.remove(key: AsyncPrimitiveKey<*, *>) = remove(key.name)
