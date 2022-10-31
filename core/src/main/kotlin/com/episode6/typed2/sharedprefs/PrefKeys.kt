package com.episode6.typed2.sharedprefs

import com.episode6.typed2.*

typealias FullPrefKey<T, BACKED_BY> = Key<T, BACKED_BY, PrefValueGetter, PrefValueSetter>
typealias PrefKey<T> = FullPrefKey<T, *>
typealias AsyncPrefKey<T> = AsyncKey<T, *, PrefValueGetter, PrefValueSetter>

interface PrefKeyBuilder : PrimitiveKeyBuilder
open class PrefKeyNamespace(private val prefix: String = "") {
  private class Builder(override val name: String) : PrefKeyBuilder

  protected fun key(name: String): PrefKeyBuilder = Builder(prefix + name)
}

fun PrefKeyBuilder.double(default: Double): FullPrefKey<Double, String?> = double().defaultProvider { default }

fun PrefKeyBuilder.stringSet(default: Set<String>): FullPrefKey<Set<String>, Set<String?>?> = stringSet().defaultProvider { default }
fun PrefKeyBuilder.stringSet(): FullPrefKey<Set<String>?, Set<String?>?> = nullableStringSet().mapType(
  mapGet = { it?.filterNotNull()?.toSet() },
  mapSet = { it }
)

fun PrefKeyBuilder.nullableStringSet(): FullPrefKey<Set<String?>?, Set<String?>?> = nativeKey(
  get = { getStringSet(name, null) },
  set = { setStringSet(name, it) },
)
