package com.episode6.typed2.bundles

import com.episode6.typed2.*
import com.episode6.typed2.nativeKey

public typealias BaseBundleKey<T, BACKED_BY> = Key<T, BACKED_BY, BaseBundleValueGetter, BaseBundleValueSetter>
public typealias AsyncBaseBundleKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, BaseBundleValueGetter, BaseBundleValueSetter>

public interface BaseBundleKeyBuilder : PrimitiveKeyBuilder

/**
 * This namespace should only be needed if you want to explicitly declare a set of keys
 * that can be shared between Bundle & PersistableBundle.
 */
public open class BaseBundleKeyNamespace(private val prefix: String = "") : RequiredEnabledKeyNamespace {
  private class Builder(override val name: String) : BaseBundleKeyBuilder

  protected fun key(name: String): BaseBundleKeyBuilder = Builder(prefix + name)
}

public fun BaseBundleKeyBuilder.double(default: Double): BaseBundleKey<Double, Double> = nativeKey(
  get = { getDouble(name, default) },
  set = { setDouble(name, it) },
  backingDefault = default,
)

public fun BaseBundleKeyBuilder.doubleArray(default: DoubleArray): BaseBundleKey<DoubleArray, DoubleArray?> = doubleArray().defaultProvider { default }
public fun BaseBundleKeyBuilder.doubleArray(): BaseBundleKey<DoubleArray?, DoubleArray?> = nativeKey(
  get = { getDoubleArray(name) },
  set = { setDoubleArray(name, it) }
)

public fun BaseBundleKeyBuilder.intArray(default: IntArray): BaseBundleKey<IntArray, IntArray?> = intArray().defaultProvider { default }
public fun BaseBundleKeyBuilder.intArray(): BaseBundleKey<IntArray?, IntArray?> = nativeKey(
  get = { getIntArray(name) },
  set = { setIntArray(name, it) }
)

public fun BaseBundleKeyBuilder.longArray(default: LongArray): BaseBundleKey<LongArray, LongArray?> = longArray().defaultProvider { default }
public fun BaseBundleKeyBuilder.longArray(): BaseBundleKey<LongArray?, LongArray?> = nativeKey(
  get = { getLongArray(name) },
  set = { setLongArray(name, it) }
)

public fun BaseBundleKeyBuilder.stringArray(default: Array<String>): BaseBundleKey<Array<String>, Array<String>?> = stringArray().defaultProvider { default }
public fun BaseBundleKeyBuilder.stringArray(): BaseBundleKey<Array<String>?, Array<String>?> = nativeKey(
  get = { getStringArray(name) },
  set = { setStringArray(name, it) }
)
