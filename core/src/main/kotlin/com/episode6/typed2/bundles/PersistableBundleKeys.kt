package com.episode6.typed2.bundles

import android.annotation.TargetApi
import android.os.PersistableBundle
import com.episode6.typed2.*

public typealias PersistableBundleKey<T, BACKED_BY> = Key<T, BACKED_BY, PersistableBundleValueGetter, PersistableBundleValueSetter>
public typealias AsyncPersistableBundleKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, PersistableBundleValueGetter, PersistableBundleValueSetter>

public interface PersistableBundleKeyBuilder : BaseBundleKeyBuilder
public open class PersistableBundleKeyNamespace(private val prefix: String = "") : RequiredEnabledKeyNamespace {
  private class Builder(override val name: String) : PersistableBundleKeyBuilder

  protected fun key(name: String): PersistableBundleKeyBuilder = Builder(prefix + name)
}

@TargetApi(22)
public fun PersistableBundleKeyBuilder.booleanArray(default: BooleanArray): PersistableBundleKey<BooleanArray, BooleanArray?> = booleanArray().defaultProvider { default }
@TargetApi(22)
public fun PersistableBundleKeyBuilder.booleanArray(): PersistableBundleKey<BooleanArray?, BooleanArray?> = nativeKey(
  get = { getBooleanArray(name) },
  set = { setBooleanArray(name, it) }
)

public fun PersistableBundleKeyBuilder.persistableBundle(default: PersistableBundle): PersistableBundleKey<PersistableBundle, PersistableBundle?> =
  persistableBundle().defaultProvider { default }

public fun PersistableBundleKeyBuilder.persistableBundle(): PersistableBundleKey<PersistableBundle?, PersistableBundle?> = nativeKey(
  get = { getPersistableBundle(name) },
  set = { setPersistableBundle(name, it) },
)
