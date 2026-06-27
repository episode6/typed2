package com.episode6.typed2.sharedprefs

import com.episode6.typed2.*

public typealias PrefKey<T, BACKED_BY> = Key<T, BACKED_BY, PrefValueGetter, PrefValueSetter>
public typealias AsyncPrefKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, PrefValueGetter, PrefValueSetter>

public interface PrefKeyBuilder : PrimitiveKeyBuilder
public open class PrefKeyNamespace(private val prefix: String = "") {
  private class Builder(override val name: String) : PrefKeyBuilder

  protected fun key(name: String): PrefKeyBuilder = Builder(prefix + name)
}

public fun PrefKeyBuilder.double(default: Double): PrefKey<Double, String?> = double().defaultProvider { default }

public fun PrefKeyBuilder.stringSet(default: Set<String>): PrefKey<Set<String>, Set<String?>?> = stringSet().defaultProvider { default }
public fun PrefKeyBuilder.stringSet(): PrefKey<Set<String>?, Set<String?>?> = nullableStringSet().mapType(
  mapGet = { it?.filterNotNull()?.toSet() },
  mapSet = { it }
)

private fun PrefKeyBuilder.nullableStringSet(): PrefKey<Set<String?>?, Set<String?>?> = nativeKey(
  get = { getStringSet(name, null) },
  set = { setStringSet(name, it) },
)
