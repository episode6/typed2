package com.episode6.typed2.bundles

import android.os.Bundle
import com.episode6.typed2.AsyncKey
import com.episode6.typed2.Key
import com.episode6.typed2.PrimitiveKeyBuilder
import com.episode6.typed2.asNonNull

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
  asNonNull { throw RequiredBundleKeyMissing(name) }

fun BundleKeyBuilder.bundle(default: Bundle): BundleKey<Bundle, Bundle?> = bundle { default }
fun BundleKeyBuilder.bundle(default: () -> Bundle): BundleKey<Bundle, Bundle?> = bundle().asNonNull(default)
fun BundleKeyBuilder.bundle(): NativeBundleKey<Bundle?> = key(
  get = { getBundle(name) },
  set = { setBundle(name, it) }
)

private fun <T : Any?> BundleKeyBuilder.key(
  get: BundleValueGetter.() -> T,
  set: BundleValueSetter.(T) -> Unit,
) = object : Key<T, BundleValueGetter, BundleValueSetter, T> {
  override val name: String = this@key.name
  override fun mapGet(backedBy: T): T = backedBy
  override fun mapSet(value: T): T = value

  override fun getBackingData(getter: BundleValueGetter): T = get.invoke(getter)
  override fun setBackingData(setter: BundleValueSetter, value: T) = set.invoke(setter, value)
}

class RequiredBundleKeyMissing(name: String) : IllegalArgumentException("Required key ($name) missing from bundle")
