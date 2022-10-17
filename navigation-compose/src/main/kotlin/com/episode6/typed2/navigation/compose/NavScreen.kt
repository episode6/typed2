package com.episode6.typed2.navigation.compose

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.episode6.typed2.*
import kotlin.reflect.KClass

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

  protected fun <T : Any, BACKED_BY : Any?> NavArg<T?, BACKED_BY>.asRequired(): NavArg<T, BACKED_BY> =
    withOutputDefault(OutputDefault.Required { RequiredNavArgumentMissing(name) })
}

internal fun <T, BACKED_BY> KeyTypeInfo<T, BACKED_BY>.toNavArgument(): NamedNavArgument = navArgument(name = name) {
  type = backingTypeInfo.kclass.asNavType()
  nullable = backingTypeInfo.nullable && default !is OutputDefault.Required
  if (!nullable && backingTypeInfo.default != null) {
    defaultValue = backingTypeInfo.default
  }
}

private fun KClass<*>.asNavType(): NavType<*> = when (this) {
  Int::class     -> NavType.IntType
  String::class  -> NavType.StringType
  Long::class    -> NavType.LongType
  Float::class   -> NavType.FloatType
  Boolean::class -> NavType.BoolType
  else           -> throw UnexpectedKeyTypeException(this)
}

class UnexpectedKeyTypeException(type: KClass<*>) : RuntimeException("Unexpected key type: $type")
class RequiredNavArgumentMissing(name: String) : RuntimeException("Required nav argument missing: $name")
