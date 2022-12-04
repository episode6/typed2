package com.episode6.typed2

interface AsyncPrimitiveKeyValueGetter : KeyValueGetter {
  suspend fun getBoolean(name: String, default: Boolean): Boolean
  suspend fun getFloat(name: String, default: Float): Float
  suspend fun getInt(name: String, default: Int): Int
  suspend fun getLong(name: String, default: Long): Long
  suspend fun getString(name: String, default: String?): String?
}

interface AsyncPrimitiveKeyValueSetter : KeyValueSetter {
  suspend fun setBoolean(name: String, value: Boolean)
  suspend fun setFloat(name: String, value: Float)
  suspend fun setInt(name: String, value: Int)
  suspend fun setLong(name: String, value: Long)
  suspend fun setString(name: String, value: String?)
}
