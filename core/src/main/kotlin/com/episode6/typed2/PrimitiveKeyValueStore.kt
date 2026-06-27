package com.episode6.typed2

public interface PrimitiveKeyValueGetter : KeyValueGetter {
  public fun getBoolean(name: String, default: Boolean): Boolean
  public fun getFloat(name: String, default: Float): Float
  public fun getInt(name: String, default: Int): Int
  public fun getLong(name: String, default: Long): Long
  public fun getString(name: String, default: String?): String?
}

public interface PrimitiveKeyValueSetter : KeyValueSetter {
  public fun setBoolean(name: String, value: Boolean)
  public fun setFloat(name: String, value: Float)
  public fun setInt(name: String, value: Int)
  public fun setLong(name: String, value: Long)
  public fun setString(name: String, value: String?)
}
