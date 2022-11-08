package com.episode6.typed2.savedstatehandle

import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.episode6.typed2.DelegateProperty
import com.episode6.typed2.bundles.*
import kotlinx.coroutines.flow.StateFlow
import java.io.Serializable
import kotlin.reflect.KClass

class TypedSavedStateHandle(private val delegate: SavedStateHandle) : BundleValueGetter, BundleValueSetter {
  override fun contains(name: String): Boolean = delegate.contains(name)
  override fun getBinder(name: String): IBinder? = delegate[name]
  override fun getBundle(name: String): Bundle? = delegate[name]
  override fun getByte(name: String, default: Byte): Byte = delegate[name] ?: default
  override fun getByteArray(name: String): ByteArray? = delegate[name]
  override fun getChar(name: String, default: Char): Char = delegate[name] ?: default
  override fun getCharArray(name: String): CharArray? = delegate[name]
  override fun getCharSequence(name: String): CharSequence? = delegate[name]
  override fun getCharSequenceArray(name: String): Array<CharSequence>? = delegate[name]
  override fun getCharSequenceArrayList(name: String): ArrayList<CharSequence>? = delegate[name]
  override fun getFloatArray(name: String): FloatArray? = delegate[name]
  override fun getIntArrayList(name: String): ArrayList<Int>? = delegate[name]
  override fun <T : Parcelable> getParcelable(name: String, kclass: KClass<T>): T? = delegate[name]
  @Suppress("UNCHECKED_CAST")
  override fun <T : Parcelable> getParcelableArray(name: String, kclass: KClass<T>, convertListToArray: List<T>.()->Array<T>): Array<T>? =
    delegate.get<Array<Parcelable>>(name)?.map { it as T }?.convertListToArray()
  override fun <T : Parcelable> getParcelableArrayList(name: String, kclass: KClass<T>): ArrayList<T>? = delegate[name]
  override fun <T : Serializable> getSerializable(name: String, kclass: KClass<T>): T? = delegate[name]
  override fun getShort(name: String, default: Short): Short = delegate[name] ?: default
  override fun getShortArray(name: String): ShortArray? = delegate[name]
  override fun getSize(name: String): Size? = delegate[name]
  override fun getSizeF(name: String): SizeF? = delegate[name]
  override fun <T : Parcelable> getSparseParcelableArray(name: String, kclass: KClass<T>): SparseArray<T>? = delegate[name]
  override fun getStringArrayList(name: String): ArrayList<String>? = delegate[name]
  override fun getBooleanArray(name: String): BooleanArray? = delegate[name]
  override fun getDouble(name: String, default: Double): Double = delegate[name] ?: default
  override fun getDoubleArray(name: String): DoubleArray? = delegate[name]
  override fun getIntArray(name: String): IntArray? = delegate[name]
  override fun getLongArray(name: String): LongArray? = delegate[name]
  override fun getStringArray(name: String): Array<String>? = delegate[name]
  override fun getBoolean(name: String, default: Boolean): Boolean = delegate[name] ?: default
  override fun getFloat(name: String, default: Float): Float = delegate[name] ?: default
  override fun getLong(name: String, default: Long): Long = delegate[name] ?: default
  override fun getInt(name: String, default: Int): Int = delegate[name] ?: default
  override fun getString(name: String, default: String?): String? = delegate[name] ?: default
  override fun remove(name: String) { delegate.remove<Any>(name) }
  override fun setBinder(name: String, value: IBinder?) { delegate[name] = value }
  override fun setBundle(name: String, value: Bundle?) { delegate[name] = value }
  override fun setByte(name: String, value: Byte) { delegate[name] = value }
  override fun setByteArray(name: String, value: ByteArray?) { delegate[name] = value }
  override fun setChar(name: String, value: Char) { delegate[name] = value }
  override fun setCharArray(name: String, value: CharArray?) { delegate[name] = value }
  override fun setCharSequence(name: String, value: CharSequence?) { delegate[name] = value }
  override fun setCharSequenceArray(name: String, value: Array<CharSequence>?) { delegate[name] = value }
  override fun setCharSequenceArrayList(name: String, value: ArrayList<CharSequence>?) { delegate[name] = value }
  override fun setFloatArray(name: String, value: FloatArray?) { delegate[name] = value }
  override fun setIntArrayList(name: String, value: ArrayList<Int>?) { delegate[name] = value }
  override fun <T : Parcelable> setParcelable(name: String, value: T?) { delegate[name] = value }
  override fun <T : Parcelable> setParcelableArray(name: String, value: Array<T>?) { delegate[name] = value?.map { it as Parcelable }?.toTypedArray<Parcelable>() }
  override fun <T : Parcelable> setParcelableArrayList(name: String, value: ArrayList<T>?) { delegate[name] = value }
  override fun <T : Serializable> setSerializable(name: String, value: T?) { delegate[name] = value }
  override fun setShort(name: String, value: Short) { delegate[name] = value }
  override fun setShortArray(name: String, value: ShortArray?) { delegate[name] = value }
  override fun setSize(name: String, value: Size?) { delegate[name] = value }
  override fun setSizeF(name: String, value: SizeF?) { delegate[name] = value }
  override fun <T : Parcelable> setSparseParcelableArray(name: String, value: SparseArray<T>?) { delegate[name] = value }
  override fun setStringArrayList(name: String, value: ArrayList<String>?) { delegate[name] = value }
  override fun setBooleanArray(name: String, value: BooleanArray?) { delegate[name] = value }
  override fun setDouble(name: String, value: Double) { delegate[name] = value }
  override fun setDoubleArray(name: String, value: DoubleArray?) { delegate[name] = value }
  override fun setIntArray(name: String, value: IntArray?) { delegate[name] = value }
  override fun setLongArray(name: String, value: LongArray?) { delegate[name] = value }
  override fun setStringArray(name: String, value: Array<String>?) { delegate[name] = value }
  override fun setInt(name: String, value: Int) { delegate[name] = value }
  override fun setString(name: String, value: String?) { delegate[name] = value }
  override fun setBoolean(name: String, value: Boolean) { delegate[name] = value }
  override fun setFloat(name: String, value: Float) { delegate[name] = value }
  override fun setLong(name: String, value: Long) { delegate[name] = value }

  @MainThread
  fun <T> getStateFlow(key: String, initialValue: T): StateFlow<T> = delegate.getStateFlow(key, initialValue)

  @MainThread
  fun <T> getLiveData(key: String, initialValue: T): MutableLiveData<T> = delegate.getLiveData(key, initialValue)
}

fun SavedStateHandle.typed(): TypedSavedStateHandle = TypedSavedStateHandle(this)

fun <T> SavedStateHandle.get(key: BundleKey<T, *>): T = typed().get(key)
fun <T> SavedStateHandle.set(key: BundleKey<T, *>, value: T) = typed().set(key, value)
fun SavedStateHandle.remove(key: BundleKey<*,*>) { remove<Any>(key.name) }
suspend fun <T> SavedStateHandle.get(key: AsyncBundleKey<T, *>): T = typed().get(key)
suspend fun <T> SavedStateHandle.set(key: AsyncBundleKey<T, *>, value: T) = typed().set(key, value)
fun SavedStateHandle.remove(key: AsyncBundleKey<*, *>) { remove<Any>(key.name) }

fun <T> TypedSavedStateHandle.property(key: BundleKey<T, *>): DelegateProperty<T> = DelegateProperty(
  get = { get(key) },
  set = { set(key, it) },
)
fun <T> SavedStateHandle.property(key: BundleKey<T, *>): DelegateProperty<T> = typed().property(key)
