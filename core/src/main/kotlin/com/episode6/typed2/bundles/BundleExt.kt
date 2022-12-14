package com.episode6.typed2.bundles

import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import com.episode6.typed2.DelegateProperty
import kotlinx.coroutines.CoroutineScope
import java.io.Serializable
import kotlin.reflect.KClass

class TypedBundle(private val delegate: Bundle) : BundleValueGetter, BundleValueSetter {
  override fun contains(name: String): Boolean = delegate.containsKey(name)
  override fun getBoolean(name: String, default: Boolean): Boolean = delegate.getBoolean(name, default)
  override fun getFloat(name: String, default: Float): Float = delegate.getFloat(name, default)
  override fun getInt(name: String, default: Int): Int = delegate.getInt(name, default)
  override fun getLong(name: String, default: Long): Long = delegate.getLong(name, default)
  override fun getString(name: String, default: String?): String? = delegate.getString(name, default)
  override fun getBinder(name: String): IBinder? = delegate.getBinder(name)
  override fun getBundle(name: String): Bundle? = delegate.getBundle(name)
  override fun getByte(name: String, default: Byte): Byte = delegate.getByte(name, default)
  override fun getByteArray(name: String): ByteArray? = delegate.getByteArray(name)
  override fun getChar(name: String, default: Char): Char = delegate.getChar(name, default)
  override fun getCharArray(name: String): CharArray? = delegate.getCharArray(name)
  override fun getCharSequence(name: String): CharSequence? = delegate.getCharSequence(name)
  override fun getCharSequenceArray(name: String): Array<CharSequence>? = delegate.getCharSequenceArray(name)
  override fun getCharSequenceArrayList(name: String): ArrayList<CharSequence>? = delegate.getCharSequenceArrayList(name)
  override fun getFloatArray(name: String): FloatArray? = delegate.getFloatArray(name)
  override fun getIntArrayList(name: String): ArrayList<Int>? = delegate.getIntegerArrayList(name)
  @Suppress("DEPRECATION")
  override fun <T : Parcelable> getParcelable(name: String, kclass: KClass<T>): T? = when {
    Build.VERSION.SDK_INT >= 33 -> delegate.getParcelable(name, kclass.java)
    else                        -> delegate.getParcelable<T>(name)
  }
  @Suppress("DEPRECATION", "UNCHECKED_CAST")
  override fun <T : Parcelable> getParcelableArray(name: String, kclass: KClass<T>, convertListToArray: List<T>.()->Array<T>): Array<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> delegate.getParcelableArray(name, kclass.java)
    else                        -> delegate.getParcelableArray(name)?.map { it as T }?.convertListToArray()
  }
  @Suppress("DEPRECATION")
  override fun <T : Parcelable> getParcelableArrayList(name: String, kclass: KClass<T>): ArrayList<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> delegate.getParcelableArrayList(name, kclass.java)
    else                        -> delegate.getParcelableArrayList<T>(name)
  }

  @Suppress("DEPRECATION", "UNCHECKED_CAST")
  override fun <T : Serializable> getSerializable(name: String, kclass: KClass<T>): T? = when {
    Build.VERSION.SDK_INT >= 33 -> delegate.getSerializable(name, kclass.java)
    else                        -> delegate.getSerializable(name) as T?
  }
  override fun getShort(name: String, default: Short): Short = delegate.getShort(name, default)
  override fun getShortArray(name: String): ShortArray? = delegate.getShortArray(name)
  override fun getSize(name: String): Size? = delegate.getSize(name)
  override fun getSizeF(name: String): SizeF? = delegate.getSizeF(name)
  @Suppress("DEPRECATION")
  override fun <T : Parcelable> getSparseParcelableArray(name: String, kclass: KClass<T>): SparseArray<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> delegate.getSparseParcelableArray(name, kclass.java)
    else                        -> delegate.getSparseParcelableArray<T>(name)
  }
  override fun getStringArrayList(name: String): ArrayList<String>? = delegate.getStringArrayList(name)
  override fun getBooleanArray(name: String): BooleanArray? = delegate.getBooleanArray(name)
  override fun getDouble(name: String, default: Double): Double = delegate.getDouble(name, default)
  override fun getDoubleArray(name: String): DoubleArray? = delegate.getDoubleArray(name)
  override fun getIntArray(name: String): IntArray? = delegate.getIntArray(name)
  override fun getLongArray(name: String): LongArray? = delegate.getLongArray(name)
  override fun getStringArray(name: String): Array<String>? = delegate.getStringArray(name)

  override fun remove(name: String) = delegate.remove(name)
  override fun setString(name: String, value: String?) = delegate.putString(name, value)
  override fun setInt(name: String, value: Int) = delegate.putInt(name, value)
  override fun setBoolean(name: String, value: Boolean) { delegate.putBoolean(name, value) }
  override fun setFloat(name: String, value: Float) { delegate.putFloat(name, value) }
  override fun setLong(name: String, value: Long) { delegate.putLong(name, value) }
  override fun setBinder(name: String, value: IBinder?) { delegate.putBinder(name, value) }
  override fun setBundle(name: String, value: Bundle?) = delegate.putBundle(name, value)
  override fun setByte(name: String, value: Byte) { delegate.putByte(name, value) }
  override fun setByteArray(name: String, value: ByteArray?) { delegate.putByteArray(name, value) }
  override fun setChar(name: String, value: Char) { delegate.putChar(name, value) }
  override fun setCharArray(name: String, value: CharArray?) { delegate.putCharArray(name, value) }
  override fun setCharSequence(name: String, value: CharSequence?) { delegate.putCharSequence(name, value) }
  override fun setCharSequenceArray(name: String, value: Array<CharSequence>?) { delegate.putCharSequenceArray(name, value) }
  override fun setCharSequenceArrayList(name: String, value: ArrayList<CharSequence>?) { delegate.putCharSequenceArrayList(name, value) }
  override fun setFloatArray(name: String, value: FloatArray?) { delegate.putFloatArray(name, value) }
  override fun setIntArrayList(name: String, value: ArrayList<Int>?) { delegate.putIntegerArrayList(name, value) }
  override fun <T : Parcelable> setParcelable(name: String, value: T?) { delegate.putParcelable(name, value) }
  override fun <T : Parcelable> setParcelableArray(name: String, value: Array<T>?) { delegate.putParcelableArray(name, value) }
  override fun <T : Parcelable> setParcelableArrayList(name: String, value: ArrayList<T>?) { delegate.putParcelableArrayList(name, value) }
  override fun <T : Serializable> setSerializable(name: String, value: T?) { delegate.putSerializable(name, value) }
  override fun setShort(name: String, value: Short) { delegate.putShort(name, value) }
  override fun setShortArray(name: String, value: ShortArray?) { delegate.putShortArray(name, value) }
  override fun setSize(name: String, value: Size?) { delegate.putSize(name, value) }
  override fun setSizeF(name: String, value: SizeF?) { delegate.putSizeF(name, value) }
  override fun <T : Parcelable> setSparseParcelableArray(name: String, value: SparseArray<T>?) { delegate.putSparseParcelableArray(name, value) }
  override fun setStringArrayList(name: String, value: ArrayList<String>?) { delegate.putStringArrayList(name, value) }
  override fun setBooleanArray(name: String, value: BooleanArray?) { delegate.putBooleanArray(name, value) }
  override fun setDouble(name: String, value: Double) { delegate.putDouble(name, value) }
  override fun setDoubleArray(name: String, value: DoubleArray?) { delegate.putDoubleArray(name, value) }
  override fun setIntArray(name: String, value: IntArray?) { delegate.putIntArray(name, value) }
  override fun setLongArray(name: String, value: LongArray?) { delegate.putLongArray(name, value) }
  override fun setStringArray(name: String, value: Array<String>?) { delegate.putStringArray(name, value) }
}

fun Bundle.typed(): TypedBundle = TypedBundle(this)

fun <T> Bundle.get(key: BundleKey<T, *>): T = typed().get(key)
fun <T> Bundle.set(key: BundleKey<T, *>, value: T) = typed().set(key, value)
fun Bundle.remove(key: BundleKey<*, *>) = typed().remove(key)
suspend fun <T> Bundle.get(key: AsyncBundleKey<T, *>): T = typed().get(key)
suspend fun <T> Bundle.set(key: AsyncBundleKey<T, *>, value: T) = typed().set(key, value)
fun Bundle.remove(key: AsyncBundleKey<*, *>) = typed().remove(key)

fun <T> TypedBundle.property(key: BundleKey<T, *>): DelegateProperty<T> = DelegateProperty(
  get = { get(key) },
  set = { set(key, it) }
)
fun <T> Bundle.property(key: BundleKey<T, *>): DelegateProperty<T> = typed().property(key)
fun <T> TypedBundle.property(key: AsyncBundleKey<T, *>, scope: CoroutineScope): DelegateProperty<T?> =
  DelegateProperty(mutableStateFlow(key, scope))
fun <T> Bundle.property(key: AsyncBundleKey<T, *>, scope: CoroutineScope): DelegateProperty<T?> = typed().property(key, scope)

inline fun <T> TypedBundle.update(key: BundleKey<T, *>, reducer: (T)->T) {
  set(key, reducer(get(key)))
}

suspend inline fun <T> TypedBundle.update(key: AsyncBundleKey<T, *>, reducer: (T)->T) {
  set(key, reducer(get(key)))
}

inline fun <T> Bundle.update(key: BundleKey<T, *>, reducer: (T)->T) = typed().update(key, reducer)
suspend inline fun <T> Bundle.update(key: AsyncBundleKey<T, *>, reducer: (T)->T) = typed().update(key, reducer)
