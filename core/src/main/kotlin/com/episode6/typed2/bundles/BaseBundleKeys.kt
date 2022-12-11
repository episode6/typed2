package com.episode6.typed2.bundles

import com.episode6.typed2.*
import com.episode6.typed2.nativeKey

typealias BaseBundleKey<T, BACKED_BY> = Key<T, BACKED_BY, BaseBundleValueGetter, BaseBundleValueSetter>
typealias AsyncBaseBundleKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, BaseBundleValueGetter, BaseBundleValueSetter>

interface BaseBundleKeyBuilder : PrimitiveKeyBuilder

/**
 * This namespace should only be needed if you want to explicitly declare a set of keys
 * that can be shared between Bundle & PersistableBundle.
 */
open class BaseBundleKeyNamespace(private val prefix: String = "") : RequiredEnabledKeyNamespace {
  private class Builder(override val name: String) : BaseBundleKeyBuilder

  protected fun key(name: String): BaseBundleKeyBuilder = Builder(prefix + name)
}

fun BaseBundleKeyBuilder.double(default: Double): BaseBundleKey<Double, Double> = nativeKey(
  get = { getDouble(name, default) },
  set = { setDouble(name, it) },
  backingDefault = default,
)

fun BaseBundleKeyBuilder.doubleArray(default: DoubleArray): BaseBundleKey<DoubleArray, DoubleArray?> = doubleArray().defaultProvider { default }
fun BaseBundleKeyBuilder.doubleArray(): BaseBundleKey<DoubleArray?, DoubleArray?> = nativeKey(
  get = { getDoubleArray(name) },
  set = { setDoubleArray(name, it) }
)

fun BaseBundleKeyBuilder.intArray(default: IntArray): BaseBundleKey<IntArray, IntArray?> = intArray().defaultProvider { default }
fun BaseBundleKeyBuilder.intArray(): BaseBundleKey<IntArray?, IntArray?> = nativeKey(
  get = { getIntArray(name) },
  set = { setIntArray(name, it) }
)

fun BaseBundleKeyBuilder.longArray(default: LongArray): BaseBundleKey<LongArray, LongArray?> = longArray().defaultProvider { default }
fun BaseBundleKeyBuilder.longArray(): BaseBundleKey<LongArray?, LongArray?> = nativeKey(
  get = { getLongArray(name) },
  set = { setLongArray(name, it) }
)

fun BaseBundleKeyBuilder.stringArray(default: Array<String>): BaseBundleKey<Array<String>, Array<String>?> = stringArray().defaultProvider { default }
fun BaseBundleKeyBuilder.stringArray(): BaseBundleKey<Array<String>?, Array<String>?> = nativeKey(
  get = { getStringArray(name) },
  set = { setStringArray(name, it) }
)
