package com.episode6.typed2

typealias PrimitiveKey<T, BACKED_BY> = Key<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter, BACKED_BY>
typealias AsyncPrimitiveKey<T, BACKED_BY> = AsyncKey<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter, BACKED_BY>
typealias NativePrimitiveKey<T> = PrimitiveKey<T, T>

interface PrimitiveKeyBuilder : KeyBuilder

fun PrimitiveKeyBuilder.string(default: String): PrimitiveKey<String, String?> = string { default }
fun PrimitiveKeyBuilder.string(default: () -> String): PrimitiveKey<String, String?> = string().withDefault(default)
fun PrimitiveKeyBuilder.string(): NativePrimitiveKey<String?> = nativeKey(
  get = { getString(name, null) },
  set = { setString(name, it) }
)

fun PrimitiveKeyBuilder.int(default: Int): NativePrimitiveKey<Int> = int { default }
fun PrimitiveKeyBuilder.int(default: () -> Int): NativePrimitiveKey<Int> = nativeKey(
  get = { getInt(name, default()) },
  set = { setInt(name, it) }
)

fun PrimitiveKeyBuilder.int(): PrimitiveKey<Int?, String?> = string().mapType(
  mapGet = { it?.toInt() },
  mapSet = { it?.toString() }
)
