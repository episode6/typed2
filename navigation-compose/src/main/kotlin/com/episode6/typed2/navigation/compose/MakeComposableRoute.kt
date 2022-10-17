package com.episode6.typed2.navigation.compose

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.episode6.typed2.KeyTypeInfo
import com.episode6.typed2.OutputDefault
import kotlin.reflect.KClass

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
