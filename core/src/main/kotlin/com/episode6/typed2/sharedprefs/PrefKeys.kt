package com.episode6.typed2.sharedprefs

import com.episode6.typed2.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

typealias PrefKey<T, BACKED_BY> = Key<T, BACKED_BY, in PrefValueGetter, in PrefValueSetter>
typealias AsyncPrefKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, in PrefValueGetter, in PrefValueSetter>
typealias NativePrefKey<T> = PrefKey<T, T>
typealias NativeAsyncPrefKey<T> = AsyncPrefKey<T, T>

interface PrefKeyBuilder : PrimitiveKeyBuilder
open class PrefKeyNamespace(private val prefix: String = "") {
  private class Builder(override val name: String) : PrefKeyBuilder

  protected fun key(name: String): PrefKeyBuilder = Builder(prefix + name)
  protected fun <T : Any?, BACKED_BY : Any?> PrefKey<T, BACKED_BY>.async(context: CoroutineContext = Dispatchers.Default): AsyncPrefKey<T, BACKED_BY> = asAsync(context)
}

fun PrefKeyBuilder.stringSet(default: Set<String>): PrefKey<Set<String>, Set<String?>?> = stringSet { default }
fun PrefKeyBuilder.stringSet(default: () -> Set<String>): PrefKey<Set<String>, Set<String?>?> = stringSet()
  .withDefault(default)

fun PrefKeyBuilder.stringSet(): PrefKey<Set<String>?, Set<String?>?> = nullableStringSet().mapType(
  mapGet = { it?.filterNotNull()?.toSet() },
  mapSet = { it }
)

fun PrefKeyBuilder.nullableStringSet(): NativePrefKey<Set<String?>?> = nativeKey(
  get = { getStringSet(name, null) },
  set = { setStringSet(name, it) },
)
