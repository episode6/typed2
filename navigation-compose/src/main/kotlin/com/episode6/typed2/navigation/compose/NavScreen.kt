package com.episode6.typed2.navigation.compose

import android.net.Uri
import com.episode6.typed2.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

typealias NavArg<T, BACKED_BY> = Key<T, BACKED_BY, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter>
typealias AsyncNavArg<T, BACKED_BY> = AsyncKey<T, BACKED_BY, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter>

interface NavArgBuilder : PrimitiveKeyBuilder
open class NavScreen(val name: String, private val argPrefix: String = "") {

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
  protected fun <T : Any, BACKED_BY : Any?> NavArg<T?, BACKED_BY>.default(default: ()->T): NavArg<T, BACKED_BY> = withDefault(default)
  protected fun <T : Any, BACKED_BY : Any?> NavArg<T?, BACKED_BY>.required(): NavArg<T, BACKED_BY> = asRequired { RequiredNavArgumentMissing(name) }
  protected fun <T : Any?, BACKED_BY : Any?> NavArg<T, BACKED_BY>.async(context: CoroutineContext = Dispatchers.Default): AsyncNavArg<T, BACKED_BY> = asAsync(context)
}

class RequiredNavArgumentMissing(name: String) : RuntimeException("Required nav argument missing: $name")
