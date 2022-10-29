package com.episode6.typed2.navigation.compose

import android.net.Uri
import com.episode6.typed2.*

typealias NavArg<T, BACKED_BY> = Key<T, BACKED_BY, PrimitiveKeyValueGetter, PrimitiveKeyValueSetter>
typealias AsyncNavArg<T, BACKED_BY> = AsyncKey<T, BACKED_BY, PrimitiveKeyValueGetter, PrimitiveKeyValueSetter>

interface NavArgBuilder : PrimitiveKeyBuilder
open class NavScreen(val name: String, private val argPrefix: String = "") : RequiredEnabledKeyNamespace {

  private val _args = LinkedHashMap<String, KeyDescriptor<*, *>>()
  internal val args: List<KeyDescriptor<*, *>> get() = _args.values.toList()

  private class Builder(
    override val name: String,
    override val newKeyCallback: (KeyDescriptor<*, *>) -> Unit,
  ) : NavArgBuilder {
    override fun String.encode(): String = Uri.encode(this)
    override fun String.decode(): String = Uri.decode(this)
  }

  protected fun key(name: String): NavArgBuilder = Builder(argPrefix + name) { _args[it.name] = it }
}

class RequiredNavArgumentMissing(name: String) : RuntimeException("Required nav argument missing: $name")
