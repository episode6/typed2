package com.episode6.typed2.bundles

import com.episode6.typed2.AsyncKey
import com.episode6.typed2.Key
import com.episode6.typed2.PrimitiveKeyBuilder
import com.episode6.typed2.nativeKey

typealias BaseBundleKey<T, BACKED_BY> = Key<T, BACKED_BY, in BaseBundleValueGetter, in BaseBundleValueSetter>
typealias AsyncBaseBundleKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, in BaseBundleValueGetter, in BaseBundleValueSetter>
typealias NativeBaseBundleKey<T> = BaseBundleKey<T, T>
typealias NativeAsyncBaseBundleKey<T> = AsyncBaseBundleKey<T, T>

interface BaseBundleKeyBuilder : PrimitiveKeyBuilder

fun BaseBundleKeyBuilder.double(default: Double): NativeBaseBundleKey<Double> = nativeKey(
  get = { getDouble(name, default) },
  set = { setDouble(name, default) },
  backingDefault = default,
)
