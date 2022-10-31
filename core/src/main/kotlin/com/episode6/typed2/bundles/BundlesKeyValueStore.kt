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

interface BundleValueGetter : BaseBundleValueGetter {
  fun getBinder(name: String): IBinder?
  fun getBooleanArray(name: String): BooleanArray?
  fun getBundle(name: String): Bundle?
  fun getByte(name: String, default: Byte): Byte
  fun getByteArray(name: String): ByteArray?
  fun getChar(name: String, default: Char): Char
  fun getCharArray(name: String): CharArray?
  fun getCharSequence(name: String): CharSequence?
  fun getCharSequenceArray(name: String): Array<CharSequence>?
  fun getCharSequenceArrayList(name: String): ArrayList<CharSequence>?
  fun getFloatArray(name: String): FloatArray?
  fun getIntArrayList(name: String): ArrayList<Int>?
  fun <T: Parcelable> getParcelable(name: String, kclass: KClass<T>): T?
  fun <T: Parcelable> getParcelableArray(name: String, kclass: KClass<T>, convertListToArray: List<T>.()->Array<T>): Array<T>?
  fun <T: Parcelable> getParcelableArrayList(name: String, kclass: KClass<T>): ArrayList<T>?
  fun <T: java.io.Serializable> getSerializable(name: String, kclass: KClass<T>): T?
  fun getShort(name: String, default: Short): Short
  fun getShortArray(name: String): ShortArray?
  fun getSize(name: String): Size?
  fun getSizeF(name: String): SizeF?
  fun <T: Parcelable> getSparseParcelableArray(name: String, kclass: KClass<T>): SparseArray<T>?
  fun getStringArrayList(name: String): ArrayList<String>?
}

interface BundleValueSetter : BaseBundleValueSetter {
  fun setBinder(name: String, value: IBinder?)
  fun setBooleanArray(name: String, value: BooleanArray?)
  fun setBundle(name: String, value: Bundle?)
  fun setByte(name: String, value: Byte)
  fun setByteArray(name: String, value: ByteArray?)
  fun setChar(name: String, value: Char)
  fun setCharArray(name: String, value: CharArray?)
  fun setCharSequence(name: String, value: CharSequence?)
  fun setCharSequenceArray(name: String, value: Array<CharSequence>?)
  fun setCharSequenceArrayList(name: String, value: ArrayList<CharSequence>?)
  fun setFloatArray(name: String, value: FloatArray?)
  fun setIntArrayList(name: String, value: ArrayList<Int>?)
  fun <T: Parcelable> setParcelable(name: String, value: T?)
  fun <T: Parcelable> setParcelableArray(name: String, value: Array<T>?)
  fun <T: Parcelable> setParcelableArrayList(name: String, value: ArrayList<T>?)
  fun <T: java.io.Serializable> setSerializable(name: String, value: T?)
  fun setShort(name: String, value: Short)
  fun setShortArray(name: String, value: ShortArray?)
  fun setSize(name: String, value: Size?)
  fun setSizeF(name: String, value: SizeF?)
  fun <T: Parcelable> setSparseParcelableArray(name: String, value: SparseArray<T>?)
  fun setStringArrayList(name: String, value: ArrayList<String>?)
}

fun <T> BundleValueGetter.get(key: BundleKey<T, *>): T = key.get(this)
fun <T> BundleValueSetter.set(key: BundleKey<T, *>, value: T) = key.set(this, value)
fun BundleValueSetter.remove(key: BundleKey<*, *>) = remove(key.name)
suspend fun <T> BundleValueGetter.get(key: AsyncBundleKey<T, *>): T = key.get(this)
suspend fun <T> BundleValueSetter.set(key: AsyncBundleKey<T, *>, value: T) = key.set(this, value)
fun BundleValueSetter.remove(key: AsyncBundleKey<*, *>) = remove(key.name)
