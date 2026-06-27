package com.episode6.typed2.bundles

import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import com.episode6.typed2.get
import com.episode6.typed2.set
import kotlin.reflect.KClass

public interface BundleValueGetter : BaseBundleValueGetter {
  public fun getBinder(name: String): IBinder?
  public fun getBooleanArray(name: String): BooleanArray?
  public fun getBundle(name: String): Bundle?
  public fun getByte(name: String, default: Byte): Byte
  public fun getByteArray(name: String): ByteArray?
  public fun getChar(name: String, default: Char): Char
  public fun getCharArray(name: String): CharArray?
  public fun getCharSequence(name: String): CharSequence?
  public fun getCharSequenceArray(name: String): Array<CharSequence>?
  public fun getCharSequenceArrayList(name: String): ArrayList<CharSequence>?
  public fun getFloatArray(name: String): FloatArray?
  public fun getIntArrayList(name: String): ArrayList<Int>?
  public fun <T: Parcelable> getParcelable(name: String, kclass: KClass<T>): T?
  public fun <T: Parcelable> getParcelableArray(name: String, kclass: KClass<T>, convertListToArray: List<T>.()->Array<T>): Array<T>?
  public fun <T: Parcelable> getParcelableArrayList(name: String, kclass: KClass<T>): ArrayList<T>?
  public fun <T: java.io.Serializable> getSerializable(name: String, kclass: KClass<T>): T?
  public fun getShort(name: String, default: Short): Short
  public fun getShortArray(name: String): ShortArray?
  public fun getSize(name: String): Size?
  public fun getSizeF(name: String): SizeF?
  public fun <T: Parcelable> getSparseParcelableArray(name: String, kclass: KClass<T>): SparseArray<T>?
  public fun getStringArrayList(name: String): ArrayList<String>?
}

public interface BundleValueSetter : BaseBundleValueSetter {
  public fun setBinder(name: String, value: IBinder?)
  public fun setBooleanArray(name: String, value: BooleanArray?)
  public fun setBundle(name: String, value: Bundle?)
  public fun setByte(name: String, value: Byte)
  public fun setByteArray(name: String, value: ByteArray?)
  public fun setChar(name: String, value: Char)
  public fun setCharArray(name: String, value: CharArray?)
  public fun setCharSequence(name: String, value: CharSequence?)
  public fun setCharSequenceArray(name: String, value: Array<CharSequence>?)
  public fun setCharSequenceArrayList(name: String, value: ArrayList<CharSequence>?)
  public fun setFloatArray(name: String, value: FloatArray?)
  public fun setIntArrayList(name: String, value: ArrayList<Int>?)
  public fun <T: Parcelable> setParcelable(name: String, value: T?)
  public fun <T: Parcelable> setParcelableArray(name: String, value: Array<T>?)
  public fun <T: Parcelable> setParcelableArrayList(name: String, value: ArrayList<T>?)
  public fun <T: java.io.Serializable> setSerializable(name: String, value: T?)
  public fun setShort(name: String, value: Short)
  public fun setShortArray(name: String, value: ShortArray?)
  public fun setSize(name: String, value: Size?)
  public fun setSizeF(name: String, value: SizeF?)
  public fun <T: Parcelable> setSparseParcelableArray(name: String, value: SparseArray<T>?)
  public fun setStringArrayList(name: String, value: ArrayList<String>?)
}

public fun <T> BundleValueGetter.get(key: BundleKey<T, *>): T = key.get(this)
public fun <T> BundleValueSetter.set(key: BundleKey<T, *>, value: T): Unit = key.set(this, value)
public fun BundleValueSetter.remove(key: BundleKey<*, *>): Unit = remove(key.name)
public suspend fun <T> BundleValueGetter.get(key: AsyncBundleKey<T, *>): T = key.get(this)
public suspend fun <T> BundleValueSetter.set(key: AsyncBundleKey<T, *>, value: T): Unit = key.set(this, value)
public fun BundleValueSetter.remove(key: AsyncBundleKey<*, *>): Unit = remove(key.name)
