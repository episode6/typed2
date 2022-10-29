package com.episode6.typed2.bundles

import android.os.Bundle
import android.os.IBinder
import com.episode6.typed2.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

typealias BundleKey<T, BACKED_BY> = Key<T, BACKED_BY, in BundleValueGetter, in BundleValueSetter>
typealias AsyncBundleKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, in BundleValueGetter, in BundleValueSetter>
typealias BundleProperty<T> = KeyValueDelegate<T, in BundleValueGetter, in BundleValueSetter>

interface BundleKeyBuilder : BaseBundleKeyBuilder
open class BundleKeyNamespace(private val prefix: String = "") {
  private class Builder(override val name: String) : BundleKeyBuilder

  protected fun key(name: String): BundleKeyBuilder = Builder(prefix + name)

  protected fun <T : Any, BACKED_BY : Any?> BundleKey<T?, BACKED_BY>.required(): BundleKey<T, BACKED_BY> = asRequired { RequiredBundleKeyMissing(name) }
  protected fun <T : Any?, BACKED_BY : Any?> BundleKey<T, BACKED_BY>.async(context: CoroutineContext = Dispatchers.Default): AsyncBundleKey<T, BACKED_BY> = asAsync(context)
}

@Suppress("UNCHECKED_CAST")
fun <T : IBinder> BundleKeyBuilder.binder(): BundleKey<T?, IBinder?> = nativeBinder().mapType(
  mapGet = { it as? T },
  mapSet = { it }
)

fun BundleKeyBuilder.bundle(default: Bundle): BundleKey<Bundle, Bundle?> = bundle { default }
fun BundleKeyBuilder.bundle(default: () -> Bundle): BundleKey<Bundle, Bundle?> = bundle().withDefault(default)
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

fun BundleKeyBuilder.byteArray(default: ByteArray): BundleKey<ByteArray, ByteArray?> = byteArray { default }
fun BundleKeyBuilder.byteArray(default: ()->ByteArray): BundleKey<ByteArray, ByteArray?> = byteArray().withDefault(default)
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

fun BundleKeyBuilder.charArray(default: CharArray): BundleKey<CharArray, CharArray?> = charArray { default }
fun BundleKeyBuilder.charArray(default: ()->CharArray): BundleKey<CharArray, CharArray?> = charArray().withDefault(default)
fun BundleKeyBuilder.charArray(): BundleKey<CharArray?, CharArray?> = nativeKey(
  get = { getCharArray(name) },
  set = { setCharArray(name, it) }
)

fun BundleKeyBuilder.charSequence(default: CharSequence): BundleKey<CharSequence, CharSequence?> = charSequence { default }
fun BundleKeyBuilder.charSequence(default: ()->CharSequence): BundleKey<CharSequence, CharSequence?> = charSequence().withDefault(default)
fun BundleKeyBuilder.charSequence(): BundleKey<CharSequence?, CharSequence?> = nativeKey(
  get = { getCharSequence(name) },
  set = { setCharSequence(name, it) }
)

fun BundleKeyBuilder.charSequenceArray(default: Array<CharSequence>): BundleKey<Array<CharSequence>, Array<CharSequence>?> = charSequenceArray { default }
fun BundleKeyBuilder.charSequenceArray(default: ()->Array<CharSequence>): BundleKey<Array<CharSequence>, Array<CharSequence>?> = charSequenceArray().withDefault(default)
fun BundleKeyBuilder.charSequenceArray(): BundleKey<Array<CharSequence>?, Array<CharSequence>?> = nativeKey(
  get = { getCharSequenceArray(name) },
  set = { setCharSequenceArray(name, it) }
)

fun BundleKeyBuilder.charSequenceArrayList(default: ArrayList<CharSequence>): BundleKey<ArrayList<CharSequence>, ArrayList<CharSequence>?> = charSequenceArrayList { default }
fun BundleKeyBuilder.charSequenceArrayList(default: ()->ArrayList<CharSequence>): BundleKey<ArrayList<CharSequence>, ArrayList<CharSequence>?> = charSequenceArrayList().withDefault(default)
fun BundleKeyBuilder.charSequenceArrayList(): BundleKey<ArrayList<CharSequence>?, ArrayList<CharSequence>?> = nativeKey(
  get = { getCharSequenceArrayList(name) },
  set = { setCharSequenceArrayList(name, it) }
)

fun BundleKeyBuilder.charSequenceList(default: List<CharSequence>): BundleKey<List<CharSequence>, ArrayList<CharSequence>?> = charSequenceList { default }
fun BundleKeyBuilder.charSequenceList(default: ()->List<CharSequence>): BundleKey<List<CharSequence>, ArrayList<CharSequence>?> = charSequenceList().withDefault(default)
fun BundleKeyBuilder.charSequenceList(): BundleKey<List<CharSequence>?, ArrayList<CharSequence>?> = charSequenceArrayList().mapType(
  mapGet = { it },
  mapSet = { it?.let { if (it is ArrayList<CharSequence>) it else ArrayList(it) } },
)

private fun BundleKeyBuilder.nativeBinder(): BundleKey<IBinder?, IBinder?> = nativeKey(
  get = { getBinder(name) },
  set = { setBinder(name, it) }
)

class RequiredBundleKeyMissing(name: String) : IllegalArgumentException("Required key ($name) missing from bundle")
