package com.episode6.typed2.bundles

import android.annotation.TargetApi
import android.os.PersistableBundle
import com.episode6.typed2.get
import com.episode6.typed2.set

public interface PersistableBundleValueGetter : BaseBundleValueGetter {
  @TargetApi(22) public fun getBooleanArray(name: String): BooleanArray?
  public fun getPersistableBundle(name: String): PersistableBundle?
}

public interface PersistableBundleValueSetter : BaseBundleValueSetter {
  @TargetApi(22) public fun setBooleanArray(name: String, value: BooleanArray?)
  public fun setPersistableBundle(name: String, value: PersistableBundle?)
}

public fun <T> PersistableBundleValueGetter.get(key: PersistableBundleKey<T, *>): T = key.get(this)
public fun <T> PersistableBundleValueSetter.set(key: PersistableBundleKey<T, *>, value: T) = key.set(this, value)
public fun PersistableBundleValueSetter.remove(key: PersistableBundleKey<*, *>) = remove(key.name)
public suspend fun <T> PersistableBundleValueGetter.get(key: AsyncPersistableBundleKey<T, *>): T = key.get(this)
public suspend fun <T> PersistableBundleValueSetter.set(key: AsyncPersistableBundleKey<T, *>, value: T) = key.set(this, value)
public fun PersistableBundleValueSetter.remove(key: AsyncPersistableBundleKey<*, *>) = remove(key.name)
