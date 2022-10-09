package com.episode6.typed2

typealias PrimitiveKey<T> = Key<T, PrimitiveKeyValueGetter, PrimitiveKeyValueSetter>
typealias AsyncPrimitiveKey<T> = AsyncKey<T, PrimitiveKeyValueGetter, PrimitiveKeyValueSetter>

interface PrimitiveKeyValueGetter : KeyValueGetter {
  override fun contains(name: String): Boolean
  fun getInt(name: String, default: Int): Int
  fun getString(name: String, default: String?): String?
}

interface PrimitiveKeyValueSetter : KeyValueSetter {
  fun setInt(name: String, value: Int)
  fun setString(name: String, value: String?)
}

fun <T> PrimitiveKeyValueGetter.get(key: PrimitiveKey<T>): T = key.get(this)
fun <T> PrimitiveKeyValueSetter.set(key: PrimitiveKey<T>, value: T) = key.set(this, value)
suspend fun <T> PrimitiveKeyValueGetter.get(key: AsyncPrimitiveKey<T>): T = key.get(this)
suspend fun <T> PrimitiveKeyValueSetter.set(key: AsyncPrimitiveKey<T>, value: T) = key.set(this, value)
