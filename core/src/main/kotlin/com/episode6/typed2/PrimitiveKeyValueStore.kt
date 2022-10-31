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
