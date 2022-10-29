package com.episode6.typed2.bundles

import android.os.Bundle
import android.os.IBinder
import com.episode6.typed2.*

typealias BundleKey<T, BACKED_BY> = Key<T, BACKED_BY, BundleValueGetter, BundleValueSetter>
typealias AsyncBundleKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, BundleValueGetter, BundleValueSetter>
typealias BundleProperty<T> = KeyValueDelegate<T, in BundleValueGetter, in BundleValueSetter>

interface BundleKeyBuilder : BaseBundleKeyBuilder
open class BundleKeyNamespace(private val prefix: String = "") : RequiredEnabledKeyNamespace {
  private class Builder(override val name: String) : BundleKeyBuilder

  protected fun key(name: String): BundleKeyBuilder = Builder(prefix + name)
}

@Suppress("UNCHECKED_CAST")
fun <T : IBinder> BundleKeyBuilder.binder(safeCast: Boolean = false): BundleKey<T?, IBinder?> = nativeBinder().mapType(
  mapGet = { if (safeCast) it as? T else it as T? },
  mapSet = { it }
)

fun BundleKeyBuilder.bundle(default: Bundle): BundleKey<Bundle, Bundle?> = bundle().defaultProvider { default }
fun BundleKeyBuilder.bundle(): BundleKey<Bundle?, Bundle?> = nativeKey(
  get = { getBundle(name) },
  set = { setBundle(name, it) },
)

fun BundleKeyBuilder.byte(default: Byte): BundleKey<Byte, Byte> = nativeKey(
  get = { getByte(name, default) },
  set = { setByte(name, it) },
  backingDefault = default
)

fun BundleKeyBuilder.byte(): BundleKey<Byte?, String?> = int().mapType(
  mapGet = { it?.toByte() },
  mapSet = { it?.toInt() }
)

fun BundleKeyBuilder.byteArray(default: ByteArray): BundleKey<ByteArray, ByteArray?> = byteArray().defaultProvider { default }
fun BundleKeyBuilder.byteArray(): BundleKey<ByteArray?, ByteArray?> = nativeKey(
  get = { getByteArray(name) },
  set = { setByteArray(name, it) }
)

fun BundleKeyBuilder.char(default: Char): BundleKey<Char, Char> = nativeKey(
  get = { getChar(name, default) },
  set = { setChar(name, it) },
  backingDefault = default
)

fun BundleKeyBuilder.char(): BundleKey<Char?, String?> = string().mapType(
  mapGet = { it?.takeIf { it.isNotEmpty() }?.get(0) },
  mapSet = { it?.toString() }
)

fun BundleKeyBuilder.charArray(default: CharArray): BundleKey<CharArray, CharArray?> = charArray().defaultProvider { default }
fun BundleKeyBuilder.charArray(): BundleKey<CharArray?, CharArray?> = nativeKey(
  get = { getCharArray(name) },
  set = { setCharArray(name, it) }
)

fun BundleKeyBuilder.charSequence(default: CharSequence): BundleKey<CharSequence, CharSequence?> = charSequence().defaultProvider { default }
fun BundleKeyBuilder.charSequence(): BundleKey<CharSequence?, CharSequence?> = nativeKey(
  get = { getCharSequence(name) },
  set = { setCharSequence(name, it) }
)

fun BundleKeyBuilder.charSequenceArray(default: Array<CharSequence>): BundleKey<Array<CharSequence>, Array<CharSequence>?> = charSequenceArray().defaultProvider { default }
fun BundleKeyBuilder.charSequenceArray(): BundleKey<Array<CharSequence>?, Array<CharSequence>?> = nativeKey(
  get = { getCharSequenceArray(name) },
  set = { setCharSequenceArray(name, it) }
)

fun BundleKeyBuilder.charSequenceList(default: List<CharSequence>): BundleKey<List<CharSequence>, ArrayList<CharSequence>?> = charSequenceList().defaultProvider { default }
fun BundleKeyBuilder.charSequenceList(): BundleKey<List<CharSequence>?, ArrayList<CharSequence>?> = charSequenceArrayList().mapType(
  mapGet = { it },
  mapSet = { it?.let { if (it is ArrayList<CharSequence>) it else ArrayList(it) } },
)

fun BundleKeyBuilder.floatArray(default: FloatArray): BundleKey<FloatArray, FloatArray?> = floatArray().defaultProvider { default }
fun BundleKeyBuilder.floatArray(): BundleKey<FloatArray?, FloatArray?> = nativeKey(
  get = { getFloatArray(name) },
  set = { setFloatArray(name, it) }
)

fun BundleKeyBuilder.intList(default: List<Int>): BundleKey<List<Int>, ArrayList<Int>?> = intList().defaultProvider { default }
fun BundleKeyBuilder.intList(): BundleKey<List<Int>?, ArrayList<Int>?> = intArrayList().mapType(
  mapGet = { it },
  mapSet = { it?.let { if (it is ArrayList<Int>) it else ArrayList(it) } },
)


private fun BundleKeyBuilder.nativeBinder(): BundleKey<IBinder?, IBinder?> = nativeKey(
  get = { getBinder(name) },
  set = { setBinder(name, it) }
)

private fun BundleKeyBuilder.charSequenceArrayList(): BundleKey<ArrayList<CharSequence>?, ArrayList<CharSequence>?> = nativeKey(
  get = { getCharSequenceArrayList(name) },
  set = { setCharSequenceArrayList(name, it) }
)

private fun BundleKeyBuilder.intArrayList(): BundleKey<ArrayList<Int>?, ArrayList<Int>?> = nativeKey(
  get = { getIntArrayList(name) },
  set = { setIntArrayList(name, it) }
)
