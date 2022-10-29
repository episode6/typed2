package com.episode6.typed2.bundles

import android.os.Bundle
import android.os.IBinder
import com.episode6.typed2.get
import com.episode6.typed2.set

interface BundleValueGetter : BaseBundleValueGetter {
  fun getBinder(name: String): IBinder? = TODO()
  fun getBundle(name: String): Bundle?
  fun getByte(name: String, default: Byte): Byte = TODO()
}

interface BundleValueSetter : BaseBundleValueSetter {
  fun setBinder(name: String, value: IBinder?) { TODO() }
  fun setBundle(name: String, value: Bundle?)
  fun setByte(name: String, value: Byte) { TODO() }
}

fun <T> BundleValueGetter.get(key: BundleKey<T, *>): T = key.get(this)
fun <T> BundleValueSetter.set(key: BundleKey<T, *>, value: T) = key.set(this, value)
fun BundleValueSetter.remove(key: BundleKey<*, *>) = remove(key.name)
suspend fun <T> BundleValueGetter.get(key: AsyncBundleKey<T, *>): T = key.get(this)
suspend fun <T> BundleValueSetter.set(key: AsyncBundleKey<T, *>, value: T) = key.set(this, value)
fun BundleValueSetter.remove(key: AsyncBundleKey<*, *>) = remove(key.name)
