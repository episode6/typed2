package com.episode6.typed2.sharedprefs

import com.episode6.typed2.AsyncKey
import com.episode6.typed2.Key
import com.episode6.typed2.PrimitiveKeyBuilder

typealias SharedPreferencesKey<T> = Key<T, in SharedPreferencesGetter, in SharedPreferencesSetter>
typealias AsyncSharedPreferencesKey<T> = AsyncKey<T, in SharedPreferencesGetter, in SharedPreferencesSetter>

interface SharedPreferencesKeyBuilder : PrimitiveKeyBuilder
open class SharedPreferencesNamespace(private val prefix: String = "") {
  private class Builder(override val name: String) : SharedPreferencesKeyBuilder
  protected fun key(name: String): SharedPreferencesKeyBuilder = Builder(prefix + name)
}

fun SharedPreferencesKeyBuilder.stringSet(default: Set<String> = emptySet()): SharedPreferencesKey<Set<String>> = key(
  get = { getStringSet(name, default)!! },
  set = { setStringSet(name, it) }
)

private fun <T : Any?> SharedPreferencesKeyBuilder.key(
  get: SharedPreferencesGetter.() -> T,
  set: SharedPreferencesSetter.(T) -> Unit,
) = object : Key<T, SharedPreferencesGetter, SharedPreferencesSetter> {
  override val name: String = this@key.name
  override fun get(getter: SharedPreferencesGetter): T = getter.get()
  override fun set(setter: SharedPreferencesSetter, value: T) = setter.set(value)
}
