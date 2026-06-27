package com.episode6.typed2.bundles

import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import com.episode6.typed2.*
import kotlin.reflect.KClass

public typealias BundleKey<T, BACKED_BY> = Key<T, BACKED_BY, BundleValueGetter, BundleValueSetter>
public typealias AsyncBundleKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, BundleValueGetter, BundleValueSetter>

public interface BundleKeyBuilder : BaseBundleKeyBuilder
public open class BundleKeyNamespace(private val prefix: String = "") : RequiredEnabledKeyNamespace {
  private class Builder(override val name: String) : BundleKeyBuilder

  protected fun key(name: String): BundleKeyBuilder = Builder(prefix + name)
}

@Suppress("UNCHECKED_CAST")
public fun <T : IBinder> BundleKeyBuilder.binder(safeCast: Boolean = false): BundleKey<T?, IBinder?> = nativeBinder().mapType(
  mapGet = { if (safeCast) it as? T else it as T? },
  mapSet = { it }
)

public fun BundleKeyBuilder.booleanArray(default: BooleanArray): BundleKey<BooleanArray, BooleanArray?> = booleanArray().defaultProvider { default }
public fun BundleKeyBuilder.booleanArray(): BundleKey<BooleanArray?, BooleanArray?> = nativeKey(
  get = { getBooleanArray(name) },
  set = { setBooleanArray(name, it) }
)

public fun BundleKeyBuilder.bundle(default: Bundle): BundleKey<Bundle, Bundle?> = bundle().defaultProvider { default }
public fun BundleKeyBuilder.bundle(): BundleKey<Bundle?, Bundle?> = nativeKey(
  get = { getBundle(name) },
  set = { setBundle(name, it) },
)

public fun BundleKeyBuilder.byte(default: Byte): BundleKey<Byte, Byte> = nativeKey(
  get = { getByte(name, default) },
  set = { setByte(name, it) },
  backingDefault = default
)

public fun BundleKeyBuilder.byte(): BundleKey<Byte?, String?> = int().mapType(
  mapGet = { it?.toByte() },
  mapSet = { it?.toInt() }
)

public fun BundleKeyBuilder.byteArray(default: ByteArray): BundleKey<ByteArray, ByteArray?> = byteArray().defaultProvider { default }
public fun BundleKeyBuilder.byteArray(): BundleKey<ByteArray?, ByteArray?> = nativeKey(
  get = { getByteArray(name) },
  set = { setByteArray(name, it) }
)

public fun BundleKeyBuilder.char(default: Char): BundleKey<Char, Char> = nativeKey(
  get = { getChar(name, default) },
  set = { setChar(name, it) },
  backingDefault = default
)

public fun BundleKeyBuilder.char(): BundleKey<Char?, String?> = string().mapType(
  mapGet = { it?.takeIf { it.isNotEmpty() }?.get(0) },
  mapSet = { it?.toString() }
)

public fun BundleKeyBuilder.charArray(default: CharArray): BundleKey<CharArray, CharArray?> = charArray().defaultProvider { default }
public fun BundleKeyBuilder.charArray(): BundleKey<CharArray?, CharArray?> = nativeKey(
  get = { getCharArray(name) },
  set = { setCharArray(name, it) }
)

public fun BundleKeyBuilder.charSequence(default: CharSequence): BundleKey<CharSequence, CharSequence?> = charSequence().defaultProvider { default }
public fun BundleKeyBuilder.charSequence(): BundleKey<CharSequence?, CharSequence?> = nativeKey(
  get = { getCharSequence(name) },
  set = { setCharSequence(name, it) }
)

public fun BundleKeyBuilder.charSequenceArray(default: Array<CharSequence>): BundleKey<Array<CharSequence>, Array<CharSequence>?> = charSequenceArray().defaultProvider { default }
public fun BundleKeyBuilder.charSequenceArray(): BundleKey<Array<CharSequence>?, Array<CharSequence>?> = nativeKey(
  get = { getCharSequenceArray(name) },
  set = { setCharSequenceArray(name, it) }
)

public fun BundleKeyBuilder.charSequenceList(default: List<CharSequence>): BundleKey<List<CharSequence>, ArrayList<CharSequence>?> = charSequenceList().defaultProvider { default }
public fun BundleKeyBuilder.charSequenceList(): BundleKey<List<CharSequence>?, ArrayList<CharSequence>?> = charSequenceArrayList().mapType(
  mapGet = { it },
  mapSet = { it?.let { if (it is ArrayList<CharSequence>) it else ArrayList(it) } },
)

public fun BundleKeyBuilder.floatArray(default: FloatArray): BundleKey<FloatArray, FloatArray?> = floatArray().defaultProvider { default }
public fun BundleKeyBuilder.floatArray(): BundleKey<FloatArray?, FloatArray?> = nativeKey(
  get = { getFloatArray(name) },
  set = { setFloatArray(name, it) }
)

public fun BundleKeyBuilder.intList(default: List<Int>): BundleKey<List<Int>, ArrayList<Int>?> = intList().defaultProvider { default }
public fun BundleKeyBuilder.intList(): BundleKey<List<Int>?, ArrayList<Int>?> = intArrayList().mapType(
  mapGet = { it },
  mapSet = { it?.let { if (it is ArrayList<Int>) it else ArrayList(it) } },
)

public inline fun <reified T : Parcelable> BundleKeyBuilder.parcelable(default: T) : BundleKey<T, T?> = parcelable<T>().defaultProvider { default }
public inline fun <reified T : Parcelable> BundleKeyBuilder.parcelable() : BundleKey<T?, T?> = NativeKeys.create(
  this,
  get = { getParcelable(name, T::class) },
  set = { setParcelable(name, it) },
)

public inline fun <reified T : Parcelable> BundleKeyBuilder.parcelableArray(default: Array<T>): BundleKey<Array<T>, Array<T>?> = parcelableArray<T>().defaultProvider { default }
public inline fun <reified T : Parcelable> BundleKeyBuilder.parcelableArray(): BundleKey<Array<T>?, Array<T>?> = NativeKeys.create(
  this,
  get = { getParcelableArray(name, T::class, convertListToArray = { toTypedArray() }) },
  set = { setParcelableArray(name, it) },
)

public inline fun <reified T : Parcelable> BundleKeyBuilder.parcelableList(default: List<T>): BundleKey<List<T>, ArrayList<T>?> = parcelableList<T>().defaultProvider { default }
public inline fun <reified T : Parcelable> BundleKeyBuilder.parcelableList(): BundleKey<List<T>?, ArrayList<T>?> = parcelableList(T::class)
public fun <T : Parcelable> BundleKeyBuilder.parcelableList(kclass: KClass<T>): BundleKey<List<T>?, ArrayList<T>?> = parcelableArrayList(kclass).mapType(
  mapGet = { it },
  mapSet = { it?.let { if (it is ArrayList<T>) it else ArrayList<T>(it) } }
)

public inline fun <reified T : java.io.Serializable> BundleKeyBuilder.serializable(default: T) : BundleKey<T, T?> = serializable<T>().defaultProvider { default }
public inline fun <reified T : java.io.Serializable> BundleKeyBuilder.serializable() : BundleKey<T?, T?> = NativeKeys.create(
  this,
  get = { getSerializable(name, T::class) },
  set = { setSerializable(name, it) },
)

public fun BundleKeyBuilder.short(default: Short): BundleKey<Short, Short> = nativeKey(
  get = { getShort(name, default) },
  set = { setShort(name, it) },
  backingDefault = default
)
public fun BundleKeyBuilder.short(): BundleKey<Short?, String?> = string().mapType(
  mapGet = { it?.toShort() },
  mapSet = { it?.toString() }
)

public fun BundleKeyBuilder.shortArray(default: ShortArray): BundleKey<ShortArray, ShortArray?> = shortArray().defaultProvider { default }
public fun BundleKeyBuilder.shortArray(): BundleKey<ShortArray?, ShortArray?> = nativeKey(
  get = { getShortArray(name) },
  set = { setShortArray(name, it) }
)

public fun BundleKeyBuilder.size(default: Size): BundleKey<Size, Size?> = size().defaultProvider { default }
public fun BundleKeyBuilder.size(): BundleKey<Size?, Size?> = nativeKey(
  get = { getSize(name) },
  set = { setSize(name, it) }
)

public fun BundleKeyBuilder.sizeF(default: SizeF): BundleKey<SizeF, SizeF?> = sizeF().defaultProvider { default }
public fun BundleKeyBuilder.sizeF(): BundleKey<SizeF?, SizeF?> = nativeKey(
  get = { getSizeF(name) },
  set = { setSizeF(name, it) }
)

public inline fun <reified T: Parcelable> BundleKeyBuilder.sparseParcelableArray(default: SparseArray<T>): BundleKey<SparseArray<T>, SparseArray<T>?> = sparseParcelableArray<T>().defaultProvider { default }
public inline fun <reified T: Parcelable> BundleKeyBuilder.sparseParcelableArray(): BundleKey<SparseArray<T>?, SparseArray<T>?> = NativeKeys.create(
  this,
  get = { getSparseParcelableArray(name, T::class) },
  set = { setSparseParcelableArray(name, it)}
)

public fun BundleKeyBuilder.stringList(default: List<String>): BundleKey<List<String>, ArrayList<String>?> = stringList().defaultProvider { default }
public fun BundleKeyBuilder.stringList(): BundleKey<List<String>?, ArrayList<String>?> = stringArrayList().mapType(
  mapGet = { it },
  mapSet = { it?.let { if (it is ArrayList<String>) it else ArrayList(it) } },
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

private fun <T : Parcelable> BundleKeyBuilder.parcelableArrayList(kclass: KClass<T>): BundleKey<ArrayList<T>?, ArrayList<T>?> = NativeKeys.create(
  this,
  get = { getParcelableArrayList(name, kclass) },
  set = { setParcelableArrayList(name, it) },
)

private fun BundleKeyBuilder.stringArrayList(): BundleKey<ArrayList<String>?, ArrayList<String>?> = nativeKey(
  get = { getStringArrayList(name) },
  set = { setStringArrayList(name, it) }
)
