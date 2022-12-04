package com.episode6.typed2

import java.math.BigDecimal

typealias PrimitiveKey<T, BACKED_BY> = Key<T, BACKED_BY, PrimitiveKeyValueGetter, PrimitiveKeyValueSetter>

interface PrimitiveKeyBuilder : KeyBuilder {
  fun String.encode(): String = this
  fun String.decode(): String = this
}

fun PrimitiveKeyBuilder.boolean(default: Boolean): PrimitiveKey<Boolean, Boolean> = nativeKey(
  get = { getBoolean(name, default) },
  set = { setBoolean(name, it) },
  backingDefault = default
)

fun PrimitiveKeyBuilder.boolean(): PrimitiveKey<Boolean?, String?> = string().mapType(
  mapGet = { it?.toBoolean() },
  mapSet = { it?.toString() }
)

fun PrimitiveKeyBuilder.float(default: Float): PrimitiveKey<Float, Float> = nativeKey(
  get = { getFloat(name, default) },
  set = { setFloat(name, it) },
  backingDefault = default
)

fun PrimitiveKeyBuilder.float(): PrimitiveKey<Float?, String?> = string().mapType(
  mapGet = { it?.toFloat() },
  mapSet = { it?.toString() }
)

fun PrimitiveKeyBuilder.int(default: Int): PrimitiveKey<Int, Int> = nativeKey(
  get = { getInt(name, default) },
  set = { setInt(name, it) },
  backingDefault = default
)

fun PrimitiveKeyBuilder.int(): PrimitiveKey<Int?, String?> = string().mapType(
  mapGet = { it?.toInt() },
  mapSet = { it?.toString() }
)

fun PrimitiveKeyBuilder.long(default: Long): PrimitiveKey<Long, Long> = nativeKey(
  get = { getLong(name, default) },
  set = { setLong(name, it) },
  backingDefault = default
)

fun PrimitiveKeyBuilder.long(): PrimitiveKey<Long?, String?> = string().mapType(
  mapGet = { it?.toLong() },
  mapSet = { it?.toString() }
)

fun PrimitiveKeyBuilder.string(default: String): PrimitiveKey<String, String> =
  nativeKey<String, PrimitiveKeyValueGetter, PrimitiveKeyValueSetter>(
    get = { getString(name, default) ?: default },
    set = { setString(name, it) },
    backingDefault = default
  ).mapType(
    mapGet = { it.decode() },
    mapSet = { it.encode() },
  )

fun PrimitiveKeyBuilder.string(): PrimitiveKey<String?, String?> =
  nativeKey<String, PrimitiveKeyValueGetter, PrimitiveKeyValueSetter>(
    get = { getString(name, null) },
    set = { setString(name, it) },
  ).mapType(
    mapGet = { it?.decode() },
    mapSet = { it?.encode() },
  )

fun PrimitiveKeyBuilder.double(): PrimitiveKey<Double?, String?> = string().mapType(
  mapGet = { it?.let { BigDecimal(it).toDouble() } },
  mapSet = { it?.toBigDecimal()?.toPlainString() },
)
