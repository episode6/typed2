package com.episode6.typed2.bundles

import com.episode6.typed2.PrimitiveKeyValueGetter
import com.episode6.typed2.PrimitiveKeyValueSetter
import com.episode6.typed2.get
import com.episode6.typed2.set


public interface BaseBundleValueGetter : PrimitiveKeyValueGetter {
  public fun getDouble(name: String, default: Double): Double
  public fun getDoubleArray(name: String): DoubleArray?
  public fun getIntArray(name: String): IntArray?
  public fun getLongArray(name: String): LongArray?
  public fun getStringArray(name: String): Array<String>?
}

public interface BaseBundleValueSetter : PrimitiveKeyValueSetter {
  public fun setDouble(name: String, value: Double)
  public fun setDoubleArray(name: String, value: DoubleArray?)
  public fun setIntArray(name: String, value: IntArray?)
  public fun setLongArray(name: String, value: LongArray?)
  public fun setStringArray(name: String, value: Array<String>?)
}
