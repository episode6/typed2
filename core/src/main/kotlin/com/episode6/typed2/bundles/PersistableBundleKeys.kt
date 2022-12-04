package com.episode6.typed2.bundles

import android.annotation.TargetApi
import android.os.PersistableBundle
import com.episode6.typed2.*

typealias PersistableBundleKey<T, BACKED_BY> = Key<T, BACKED_BY, PersistableBundleValueGetter, PersistableBundleValueSetter>
typealias AsyncPersistableBundleKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, PersistableBundleValueGetter, PersistableBundleValueSetter>

interface PersistableBundleKeyBuilder : BaseBundleKeyBuilder
open class PersistableBundleKeyNamespace(private val prefix: String = "") : RequiredEnabledKeyNamespace, AsyncMappingInlineBackingNamespace {
  private class Builder(override val name: String) : PersistableBundleKeyBuilder

  protected fun key(name: String): PersistableBundleKeyBuilder = Builder(prefix + name)
}

@TargetApi(22)
fun PersistableBundleKeyBuilder.booleanArray(default: BooleanArray): PersistableBundleKey<BooleanArray, BooleanArray?> = booleanArray().defaultProvider { default }
@TargetApi(22)
fun PersistableBundleKeyBuilder.booleanArray(): PersistableBundleKey<BooleanArray?, BooleanArray?> = nativeKey(
  get = { getBooleanArray(name) },
  set = { setBooleanArray(name, it) }
)

fun PersistableBundleKeyBuilder.persistableBundle(default: PersistableBundle): PersistableBundleKey<PersistableBundle, PersistableBundle?> =
  persistableBundle().defaultProvider { default }

fun PersistableBundleKeyBuilder.persistableBundle(): PersistableBundleKey<PersistableBundle?, PersistableBundle?> = nativeKey(
  get = { getPersistableBundle(name) },
  set = { setPersistableBundle(name, it) },
)
