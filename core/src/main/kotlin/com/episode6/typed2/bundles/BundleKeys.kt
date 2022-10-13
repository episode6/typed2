package com.episode6.typed2.bundles

import android.os.Bundle
import com.episode6.typed2.*
import kotlin.collections.withDefault

typealias BundleKey<T, BACKED_BY> = Key<T, in BundleValueGetter, in BundleValueSetter, BACKED_BY>
typealias AsyncBundleKey<T, BACKED_BY> = AsyncKey<T, in BundleValueGetter, in BundleValueSetter, BACKED_BY>
typealias NativeBundleKey<T> = BundleKey<T, T>
typealias NativeAsyncBundleKey<T> = AsyncBundleKey<T, T>

interface BundleKeyBuilder : PrimitiveKeyBuilder
open class BundleKeyNamespace(private val prefix: String = "") {
  private class Builder(override val name: String) : BundleKeyBuilder

  protected fun key(name: String): BundleKeyBuilder = Builder(prefix + name)
}

fun <T : Any, BACKED_BY : Any?> BundleKey<T?, BACKED_BY>.asRequired(): BundleKey<T, BACKED_BY> =
  withDefault { throw RequiredBundleKeyMissing(name) }

fun BundleKeyBuilder.bundle(default: Bundle): BundleKey<Bundle, Bundle?> = bundle { default }
fun BundleKeyBuilder.bundle(default: () -> Bundle): BundleKey<Bundle, Bundle?> = bundle().withDefault(default)
fun BundleKeyBuilder.bundle(): NativeBundleKey<Bundle?> = nativeKey(
  get = { getBundle(name) },
  set = { setBundle(name, it) }
)

class RequiredBundleKeyMissing(name: String) : IllegalArgumentException("Required key ($name) missing from bundle")
