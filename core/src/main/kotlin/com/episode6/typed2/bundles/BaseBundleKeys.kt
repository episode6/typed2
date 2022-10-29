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
  set = { setDouble(name, default) },
  backingDefault = default,
)
