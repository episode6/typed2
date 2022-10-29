package com.episode6.typed2.bundles

import android.os.PersistableBundle
import com.episode6.typed2.*

typealias PersistableBundleKey<T, BACKED_BY> = Key<T, BACKED_BY, PersistableBundleValueGetter, PersistableBundleValueSetter>
typealias AsyncPersistableBundleKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, PersistableBundleValueGetter, PersistableBundleValueSetter>
typealias PersistableBundleProperty<T> = KeyValueDelegate<T, in PersistableBundleValueGetter, PersistableBundleValueSetter>

interface PersistableBundleKeyBuilder : BaseBundleKeyBuilder
open class PersistableBundleKeyNamespace(private val prefix: String = "") : RequiredEnabledKeyNamespace {
  private class Builder(override val name: String) : PersistableBundleKeyBuilder

  protected fun key(name: String): PersistableBundleKeyBuilder = Builder(prefix + name)
}

fun PersistableBundleKeyBuilder.persistableBundle(default: PersistableBundle): PersistableBundleKey<PersistableBundle, PersistableBundle?> =
  persistableBundle().defaultProvider { default }

fun PersistableBundleKeyBuilder.persistableBundle(): PersistableBundleKey<PersistableBundle?, PersistableBundle?> = nativeKey(
  get = { getPersistableBundle(name) },
  set = { setPersistableBundle(name, it) },
)
