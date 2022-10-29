package com.episode6.typed2.bundles

import android.os.Bundle
import android.os.IBinder
import com.episode6.typed2.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

typealias BundleKey<T, BACKED_BY> = Key<T, BACKED_BY, in BundleValueGetter, in BundleValueSetter>
typealias AsyncBundleKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, in BundleValueGetter, in BundleValueSetter>
typealias NativeBundleKey<T> = BundleKey<T, T>
typealias NativeAsyncBundleKey<T> = AsyncBundleKey<T, T>
typealias BundleProperty<T> = KeyValueDelegate<T, in BundleValueGetter, in BundleValueSetter>

interface BundleKeyBuilder : BaseBundleKeyBuilder
open class BundleKeyNamespace(private val prefix: String = "") {
  private class Builder(override val name: String) : BundleKeyBuilder

  protected fun key(name: String): BundleKeyBuilder = Builder(prefix + name)

  protected fun <T : Any, BACKED_BY : Any?> BundleKey<T?, BACKED_BY>.required(): BundleKey<T, BACKED_BY> = asRequired { RequiredBundleKeyMissing(name) }
  protected fun <T : Any?, BACKED_BY : Any?> BundleKey<T, BACKED_BY>.async(context: CoroutineContext = Dispatchers.Default): AsyncBundleKey<T, BACKED_BY> = asAsync(context)
}

fun <T: IBinder> BundleKeyBuilder.binder(): BundleKey<T?, IBinder?> = nativeBinder().cast()
private fun BundleKeyBuilder.nativeBinder(): NativeBundleKey<IBinder?> = nativeKey(
  get = { getBinder(name) },
  set = { setBinder(name, it) }
)

fun BundleKeyBuilder.bundle(default: Bundle): BundleKey<Bundle, Bundle?> = bundle { default }
fun BundleKeyBuilder.bundle(default: () -> Bundle): BundleKey<Bundle, Bundle?> = bundle().withDefault(default)
fun BundleKeyBuilder.bundle(): NativeBundleKey<Bundle?> = nativeKey(
  get = { getBundle(name) },
  set = { setBundle(name, it) },
)

fun BundleKeyBuilder.byte(default: Byte): NativeBundleKey<Byte> = nativeKey(
  get = { getByte(name, default) },
  set = { setByte(name, it) },
  backingDefault = default
)
fun BundleKeyBuilder.byte(): BundleKey<Byte?, String?> = int().mapType(
  mapGet = { it?.toByte() },
  mapSet = { it?.toInt() }
)

class RequiredBundleKeyMissing(name: String) : IllegalArgumentException("Required key ($name) missing from bundle")
