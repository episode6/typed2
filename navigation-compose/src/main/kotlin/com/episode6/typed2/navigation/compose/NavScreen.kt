package com.episode6.typed2.navigation.compose

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.episode6.typed2.*
import kotlin.reflect.KType

typealias NavArg<T, BACKED_BY> = Key<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter, BACKED_BY>
typealias AsyncNavArg<T, BACKED_BY> = AsyncKey<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter, BACKED_BY>
typealias NativeNavArg<T> = NavArg<T, T>
typealias NativeAsyncNavArg<T> = AsyncNavArg<T, T>

interface NavArgBuilder : PrimitiveKeyBuilder
open class NavScreen(val name: String, private val argPrefix: String = "") {
  private class Builder(override val name: String) : NavArgBuilder {
    override val stringsShouldBeEncoded: Boolean = true
  }


  private val _args = mutableListOf<KeyTypeInfo<*, *>>()
  internal val args: List<KeyTypeInfo<*, *>> get() = _args.toList()

  protected fun <T, BACKED_BY> arg(
    name: String,
    argBuilder: NavArgBuilder.() -> NavArg<T, BACKED_BY>,
  ): NavArg<T, BACKED_BY> = Builder(argPrefix + name).argBuilder().also { _args += it }

  protected fun <T, BACKED_BY> asyncArg(
    name: String,
    argBuilder: NavArgBuilder.() -> NavArg<T, BACKED_BY>,
  ): AsyncNavArg<T, BACKED_BY> = Builder(argPrefix + name).argBuilder().asAsync().also { _args += it }
}

internal fun <T, BACKED_BY> KeyTypeInfo<T, BACKED_BY>.toNavArgument(): NamedNavArgument = navArgument(name = name) {
  val backingType = backingTypeInfo.type()
  type = backingType.asNavType()
  nullable = backingType.isMarkedNullable && outputDefault !is OutputDefault.Required
  if (!nullable) {
    backingTypeInfo.default?.let { defaultValue = it }
  }
}

private fun KType.asNavType(): NavType<*> = when (classifier) {
  Int::class     -> NavType.IntType
  String::class  -> NavType.StringType
  Long::class    -> NavType.LongType
  Float::class   -> NavType.FloatType
  Boolean::class -> NavType.BoolType
  else           -> throw UnexpectedKeyTypeException(this)
}

class UnexpectedKeyTypeException(type: KType) : RuntimeException("Unexpected key type: $type")
