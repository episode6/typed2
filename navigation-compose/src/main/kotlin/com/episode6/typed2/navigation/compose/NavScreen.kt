package com.episode6.typed2.navigation.compose

import com.episode6.typed2.*

typealias NavArg<T, BACKED_BY> = Key<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter, BACKED_BY>
typealias AsyncNavArg<T, BACKED_BY> = AsyncKey<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter, BACKED_BY>
typealias NativeNavArg<T> = NavArg<T, T>
typealias NativeAsyncNavArg<T> = AsyncNavArg<T, T>

interface NavArgBuilder : PrimitiveKeyBuilder
open class NavScreen(val name: String, argPrefix: String = "") {
  private class Builder(override val name: String) : NavArgBuilder {
    override val stringsShouldBeEncoded: Boolean = true
  }


  private val _args = mutableListOf<Arg>()
  internal val args: List<Arg> get() = _args.toList()

  protected fun <T, BACKED_BY> arg(
    name: String,
    argBuilder: NavArgBuilder.() -> NavArg<T, BACKED_BY>,
  ): NavArg<T, BACKED_BY> = Builder(name).argBuilder().also { _args += Arg.Sync(it) }

  protected fun <T, BACKED_BY> asyncArg(
    name: String,
    argBuilder: NavArgBuilder.() -> NavArg<T, BACKED_BY>,
  ): AsyncNavArg<T, BACKED_BY> = Builder(name).argBuilder().asAsync().also { _args += Arg.Async(it) }
}

internal sealed interface Arg {
  data class Sync<T, BACKED_BY>(val key: NavArg<T, BACKED_BY>) : Arg
  data class Async<T, BACKED_BY>(val key: AsyncNavArg<T, BACKED_BY>) : Arg
}
