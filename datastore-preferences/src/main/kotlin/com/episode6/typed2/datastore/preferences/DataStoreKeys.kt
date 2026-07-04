package com.episode6.typed2.datastore.preferences

import com.episode6.typed2.*
import kotlin.coroutines.EmptyCoroutineContext

public typealias DataStoreKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, DataStoreValueGetter, DataStoreValueSetter>

public interface DataStoreKeyBuilder : PrimitiveKeyBuilder
public open class DataStoreKeyNamespace(private val prefix: String = "") {
  private class Builder(override val name: String) : DataStoreKeyBuilder

  protected fun key(name: String): DataStoreKeyBuilder = Builder(prefix + name)
}

public fun DataStoreKeyBuilder.boolean(default: Boolean): DataStoreKey<Boolean, Boolean> = primitive().boolean(default).async(EmptyCoroutineContext)
public fun DataStoreKeyBuilder.boolean(): DataStoreKey<Boolean?, String?> = primitive().boolean().async(EmptyCoroutineContext)

public fun DataStoreKeyBuilder.float(default: Float): DataStoreKey<Float, Float> = primitive().float(default).async(EmptyCoroutineContext)
public fun DataStoreKeyBuilder.float(): DataStoreKey<Float?, String?> = primitive().float().async(EmptyCoroutineContext)

public fun DataStoreKeyBuilder.int(default: Int): DataStoreKey<Int, Int> = primitive().int(default).async(EmptyCoroutineContext)
public fun DataStoreKeyBuilder.int(): DataStoreKey<Int?, String?> = primitive().int().async(EmptyCoroutineContext)

public fun DataStoreKeyBuilder.long(default: Long): DataStoreKey<Long, Long> = primitive().long(default).async(EmptyCoroutineContext)
public fun DataStoreKeyBuilder.long(): DataStoreKey<Long?, String?> = primitive().long().async(EmptyCoroutineContext)

public fun DataStoreKeyBuilder.string(default: String): DataStoreKey<String, String> = primitive().string(default).async(EmptyCoroutineContext)
public fun DataStoreKeyBuilder.string(): DataStoreKey<String?, String?> = primitive().string().async(EmptyCoroutineContext)

public fun DataStoreKeyBuilder.double(default: Double): DataStoreKey<Double, String?> = double().defaultProvider { default }
public fun DataStoreKeyBuilder.double(): DataStoreKey<Double?, String?> = primitive().double().async(EmptyCoroutineContext)

public fun DataStoreKeyBuilder.stringSet(default: Set<String>): DataStoreKey<Set<String>, Set<String?>?> = stringSet().defaultProvider { default }
public fun DataStoreKeyBuilder.stringSet(): DataStoreKey<Set<String>?, Set<String?>?> = nullableStringSet().mapType(
  mapGet = { it?.filterNotNull()?.toSet() },
  mapSet = { it },
).async(EmptyCoroutineContext)

private fun DataStoreKeyBuilder.nullableStringSet(): Key<Set<String?>?, Set<String?>?, DataStoreValueGetter, DataStoreValueSetter> =
  NativeKeys.create<Set<String?>, DataStoreValueGetter, DataStoreValueSetter>(
    this,
    get = { getStringSet(name, null) },
    set = { setStringSet(name, it) },
  )

// upcast so the primitive builder extensions from core resolve instead of the same-signature extensions in this file
private fun DataStoreKeyBuilder.primitive(): PrimitiveKeyBuilder = this
