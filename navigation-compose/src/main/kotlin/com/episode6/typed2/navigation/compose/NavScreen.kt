package com.episode6.typed2.navigation.compose

import com.episode6.typed2.*

typealias NavArg<T, BACKED_BY> = Key<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter, BACKED_BY>
typealias AsyncNavArg<T, BACKED_BY> = AsyncKey<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter, BACKED_BY>
typealias NativeNavArg<T> = NavArg<T, T>
typealias NativeAsyncNavArg<T> = AsyncNavArg<T, T>

interface NavArgBuilder : PrimitiveKeyBuilder
open class NavScreen(val name: String, private val argPrefix: String = "") {

  private val _args = LinkedHashMap<String, KeyTypeInfo<*, *>>()
  internal val args: List<KeyTypeInfo<*, *>> get() = _args.values.toList()

  private class Builder(
    override val name: String,
    override val stringsShouldBeEncoded: Boolean = true,
    override val newKeyCallback: (KeyTypeInfo<*, *>) -> Unit,
  ) : NavArgBuilder

  protected fun key(name: String): NavArgBuilder = Builder(argPrefix + name) { _args[it.name] = it }

  protected fun <T : Any, BACKED_BY : Any?> NavArg<T?, BACKED_BY>.required(): NavArg<T, BACKED_BY> =
    withOutputDefault(OutputDefault.Required { RequiredNavArgumentMissing(name) })
}

class RequiredNavArgumentMissing(name: String) : RuntimeException("Required nav argument missing: $name")
