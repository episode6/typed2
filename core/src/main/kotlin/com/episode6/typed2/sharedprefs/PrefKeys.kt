package com.episode6.typed2.sharedprefs

import com.episode6.typed2.*

typealias PrefKey<T, BACKED_BY> = Key<T, in PrefValueGetter, in PrefValueSetter, BACKED_BY>
typealias AsyncPrefKey<T, BACKED_BY> = AsyncKey<T, in PrefValueGetter, in PrefValueSetter, BACKED_BY>

interface PrefKeyBuilder : PrimitiveKeyBuilder
open class PrefNamespace(private val prefix: String = "") {
  private class Builder(override val name: String) : PrefKeyBuilder

  protected fun key(name: String): PrefKeyBuilder = Builder(prefix + name)
}

fun PrefKeyBuilder.stringSet(default: Set<String>): PrefKey<Set<String>, Set<String?>?> = stringSet { default }
fun PrefKeyBuilder.stringSet(default: () -> Set<String>): PrefKey<Set<String>, Set<String?>?> = stringSet().asNonNull(default)
fun PrefKeyBuilder.stringSet(): PrefKey<Set<String>?, Set<String?>?> = nullableStringSet().mapType(
  mapGet = { it?.filterNotNull()?.toSet() },
  mapSet = { it }
)
fun PrefKeyBuilder.nullableStringSet(): PrefKey<Set<String?>?, Set<String?>?> = key(
  get = { getStringSet(name, null) },
  set = { setStringSet(name, it) }
)

private fun <T : Any?, BACKED_BY: Any?> PrefKeyBuilder.key(
  get: PrefValueGetter.() -> T,
  set: PrefValueSetter.(T) -> Unit,
) = object : Key<T, PrefValueGetter, PrefValueSetter, BACKED_BY> {
  override val name: String = this@key.name
  override fun get(getter: PrefValueGetter): T = getter.get()
  override fun set(setter: PrefValueSetter, value: T) = setter.set(value)
}
