package com.episode6.typed2.bundles

import android.os.PersistableBundle
import com.episode6.typed2.get
import com.episode6.typed2.set

interface PersistableBundleValueGetter : BaseBundleValueGetter {
  fun getBooleanArray(name: String): BooleanArray?
  fun getPersistableBundle(name: String): PersistableBundle?
}

interface PersistableBundleValueSetter : BaseBundleValueSetter {
  fun setBooleanArray(name: String, value: BooleanArray?)
  fun setPersistableBundle(name: String, value: PersistableBundle?)
}

fun <T> PersistableBundleValueGetter.get(key: PersistableBundleKey<T, *>): T = key.get(this)
fun <T> PersistableBundleValueSetter.set(key: PersistableBundleKey<T, *>, value: T) = key.set(this, value)
fun PersistableBundleValueSetter.remove(key: PersistableBundleKey<*, *>) = remove(key.name)
suspend fun <T> PersistableBundleValueGetter.get(key: AsyncPersistableBundleKey<T, *>): T = key.get(this)
suspend fun <T> PersistableBundleValueSetter.set(key: AsyncPersistableBundleKey<T, *>, value: T) = key.set(this, value)
fun PersistableBundleValueSetter.remove(key: AsyncPersistableBundleKey<*, *>) = remove(key.name)
