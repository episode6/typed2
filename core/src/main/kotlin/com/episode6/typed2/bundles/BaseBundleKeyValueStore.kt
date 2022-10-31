package com.episode6.typed2.bundles

import com.episode6.typed2.PrimitiveKeyValueGetter
import com.episode6.typed2.PrimitiveKeyValueSetter
import com.episode6.typed2.get
import com.episode6.typed2.set


interface BaseBundleValueGetter : PrimitiveKeyValueGetter {
  fun getDouble(name: String, default: Double): Double
  fun getDoubleArray(name: String): DoubleArray?
  fun getIntArray(name: String): IntArray?
  fun getLongArray(name: String): LongArray?
  fun getStringArray(name: String): Array<String>?
}

interface BaseBundleValueSetter : PrimitiveKeyValueSetter {
  fun setDouble(name: String, value: Double)
  fun setDoubleArray(name: String, value: DoubleArray?)
  fun setIntArray(name: String, value: IntArray?)
  fun setLongArray(name: String, value: LongArray?)
  fun setStringArray(name: String, value: Array<String>?)
}

fun <T> BaseBundleValueGetter.get(key: BaseBundleKey<T, *>): T = key.get(this)
fun <T> BaseBundleValueSetter.set(key: BaseBundleKey<T, *>, value: T) = key.set(this, value)
fun BaseBundleValueSetter.remove(key: BaseBundleKey<*, *>) = remove(key.name)
suspend fun <T> BaseBundleValueGetter.get(key: AsyncBaseBundleKey<T, *>): T = key.get(this)
suspend fun <T> BaseBundleValueSetter.set(key: AsyncBaseBundleKey<T, *>, value: T) = key.set(this, value)
fun BaseBundleValueSetter.remove(key: AsyncBaseBundleKey<*, *>) = remove(key.name)
