package com.episode6.typed2.sharedprefs

import com.episode6.typed2.AsyncKey
import com.episode6.typed2.Key
import com.episode6.typed2.PrimitiveKeyBuilder

typealias PrefKey<T> = Key<T, in PrefValueGetter, in PrefValueSetter>
typealias AsyncPrefKey<T> = AsyncKey<T, in PrefValueGetter, in PrefValueSetter>

interface PrefKeyBuilder : PrimitiveKeyBuilder
open class PrefNamespace(private val prefix: String = "") {
  private class Builder(override val name: String) : PrefKeyBuilder
  protected fun key(name: String): PrefKeyBuilder = Builder(prefix + name)
}

fun PrefKeyBuilder.stringSet(default: Set<String> = emptySet()): PrefKey<Set<String>> = stringSet { default }
fun PrefKeyBuilder.stringSet(default: ()->Set<String>): PrefKey<Set<String>> = key(
  get = { getStringSet(name, default())!! },
  set = { setStringSet(name, it) }
)

private fun <T : Any?> PrefKeyBuilder.key(
  get: PrefValueGetter.() -> T,
  set: PrefValueSetter.(T) -> Unit,
) = object : Key<T, PrefValueGetter, PrefValueSetter> {
  override val name: String = this@key.name
  override fun get(getter: PrefValueGetter): T = getter.get()
  override fun set(setter: PrefValueSetter, value: T) = setter.set(value)
}
