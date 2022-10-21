package com.episode6.typed2.bundles

import android.os.Bundle
import com.episode6.typed2.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

typealias BundleKey<T, BACKED_BY> = Key<T, BACKED_BY, in BundleValueGetter, in BundleValueSetter>
typealias AsyncBundleKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, in BundleValueGetter, in BundleValueSetter>
typealias NativeBundleKey<T> = BundleKey<T, T>
typealias NativeAsyncBundleKey<T> = AsyncBundleKey<T, T>
typealias BundleProperty<T> = KeyValueDelegate<T, in BundleValueGetter, in BundleValueSetter>

interface BundleKeyBuilder : PrimitiveKeyBuilder
open class BundleKeyNamespace(private val prefix: String = "") {
  private class Builder(override val name: String) : BundleKeyBuilder

  protected fun key(name: String): BundleKeyBuilder = Builder(prefix + name)

  protected fun <T : Any, BACKED_BY : Any?> BundleKey<T?, BACKED_BY>.required(): BundleKey<T, BACKED_BY> = asRequired { RequiredBundleKeyMissing(name) }
  protected fun <T : Any?, BACKED_BY : Any?> BundleKey<T, BACKED_BY>.async(context: CoroutineContext = Dispatchers.Default): AsyncBundleKey<T, BACKED_BY> = asAsync(context)
}

fun BundleKeyBuilder.bundle(default: Bundle): BundleKey<Bundle, Bundle?> = bundle { default }
fun BundleKeyBuilder.bundle(default: () -> Bundle): BundleKey<Bundle, Bundle?> = bundle()
  .withDefault(default)

fun BundleKeyBuilder.bundle(): NativeBundleKey<Bundle?> = nativeKey(
  get = { getBundle(name) },
  set = { setBundle(name, it) },
)

class RequiredBundleKeyMissing(name: String) : IllegalArgumentException("Required key ($name) missing from bundle")
