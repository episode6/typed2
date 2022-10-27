package com.episode6.typed2.bundles

import android.os.PersistableBundle
import com.episode6.typed2.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

typealias PersistableBundleKey<T, BACKED_BY> = Key<T, BACKED_BY, in PersistableBundleValueGetter, in PersistableBundleValueSetter>
typealias AsyncPersistableBundleKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, in PersistableBundleValueGetter, in PersistableBundleValueSetter>
typealias NativePersistableBundleKey<T> = PersistableBundleKey<T, T>
typealias NativeAsyncPersistableBundleKey<T> = AsyncPersistableBundleKey<T, T>
typealias PersistableBundleProperty<T> = KeyValueDelegate<T, in PersistableBundleValueGetter, in PersistableBundleValueSetter>

interface PersistableBundleKeyBuilder : BaseBundleKeyBuilder
open class PersistableBundleKeyNamespace(private val prefix: String = "") {
  private class Builder(override val name: String) : PersistableBundleKeyBuilder

  protected fun key(name: String): PersistableBundleKeyBuilder = Builder(prefix + name)
  protected fun <T : Any, BACKED_BY : Any?> PersistableBundleKey<T?, BACKED_BY>.required(): PersistableBundleKey<T, BACKED_BY> = asRequired { RequiredPersistableBundleKeyMissing(name) }
  protected fun <T : Any?, BACKED_BY : Any?> PersistableBundleKey<T, BACKED_BY>.async(context: CoroutineContext = Dispatchers.Default): AsyncPersistableBundleKey<T, BACKED_BY> = asAsync(context)
}

fun PersistableBundleKeyBuilder.persistableBundle(default: PersistableBundle): PersistableBundleKey<PersistableBundle, PersistableBundle?> = persistableBundle { default }
fun PersistableBundleKeyBuilder.persistableBundle(default: () -> PersistableBundle): PersistableBundleKey<PersistableBundle, PersistableBundle?> = persistableBundle().withDefault(default)
fun PersistableBundleKeyBuilder.persistableBundle(): NativePersistableBundleKey<PersistableBundle?> = nativeKey(
  get = { getPersistableBundle(name) },
  set = { setPersistableBundle(name, it) },
)

class RequiredPersistableBundleKeyMissing(name: String) : IllegalArgumentException("Required key ($name) missing from persistable bundle")