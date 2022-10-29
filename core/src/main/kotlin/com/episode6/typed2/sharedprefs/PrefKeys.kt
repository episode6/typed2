package com.episode6.typed2.sharedprefs

import com.episode6.typed2.*

typealias PrefKey<T, BACKED_BY> = Key<T, BACKED_BY, PrefValueGetter, PrefValueSetter>
typealias AsyncPrefKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, PrefValueGetter, PrefValueSetter>

interface PrefKeyBuilder : PrimitiveKeyBuilder
open class PrefKeyNamespace(private val prefix: String = "") {
  private class Builder(override val name: String) : PrefKeyBuilder

  protected fun key(name: String): PrefKeyBuilder = Builder(prefix + name)
  protected fun <T : Any, BACKED_BY : Any> PrefKey<T?, BACKED_BY>.default(default: () -> T): PrefKey<T, BACKED_BY> = withDefault(default)
}

fun PrefKeyBuilder.double(default: Double): PrefKey<Double, String?> = double().withDefault { default }

fun PrefKeyBuilder.stringSet(default: Set<String>): PrefKey<Set<String>, Set<String?>?> = stringSet().withDefault { default }
fun PrefKeyBuilder.stringSet(): PrefKey<Set<String>?, Set<String?>?> = nullableStringSet().mapType(
  mapGet = { it?.filterNotNull()?.toSet() },
  mapSet = { it }
)

fun PrefKeyBuilder.nullableStringSet(): PrefKey<Set<String?>?, Set<String?>?> = nativeKey(
  get = { getStringSet(name, null) },
  set = { setStringSet(name, it) },
)
