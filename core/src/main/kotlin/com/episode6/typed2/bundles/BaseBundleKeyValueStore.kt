package com.episode6.typed2.bundles

import com.episode6.typed2.PrimitiveKeyValueGetter
import com.episode6.typed2.PrimitiveKeyValueSetter
import com.episode6.typed2.get
import com.episode6.typed2.set


interface BaseBundleValueGetter : PrimitiveKeyValueGetter {
  fun getDouble(name: String, default: Double): Double
}

interface BaseBundleValueSetter : PrimitiveKeyValueSetter {
  fun setDouble(name: String, value: Double)
}

fun <T> BaseBundleValueGetter.get(key: BaseBundleKey<T, *>): T = key.get(this)
fun <T> BaseBundleValueSetter.set(key: BaseBundleKey<T, *>, value: T) = key.set(this, value)
fun BaseBundleValueSetter.remove(key: BaseBundleKey<*, *>) = remove(key.name)
suspend fun <T> BaseBundleValueGetter.get(key: AsyncBaseBundleKey<T, *>): T = key.get(this)
suspend fun <T> BaseBundleValueSetter.set(key: AsyncBaseBundleKey<T, *>, value: T) = key.set(this, value)
fun BaseBundleValueSetter.remove(key: AsyncBaseBundleKey<*, *>) = remove(key.name)
