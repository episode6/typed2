package com.episode6.typed2

import android.net.Uri

typealias PrimitiveKey<T, BACKED_BY> = Key<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter, BACKED_BY>
typealias AsyncPrimitiveKey<T, BACKED_BY> = AsyncKey<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter, BACKED_BY>
typealias NativePrimitiveKey<T> = PrimitiveKey<T, T>

interface PrimitiveKeyBuilder : KeyBuilder

fun PrimitiveKeyBuilder.string(uriEncoded: Boolean = false, default: String): NativePrimitiveKey<String> = string(uriEncoded) { default }
fun PrimitiveKeyBuilder.string(uriEncoded: Boolean = false, default: () -> String): NativePrimitiveKey<String> =
  nativeKey<String, PrimitiveKeyValueGetter, PrimitiveKeyValueSetter>(
  get = { getString(name, default()) ?: default() },
  set = { setString(name, it) },
  backingDefault = default
).let { key ->
  when (uriEncoded) {
    false -> key
    true  -> key.mapType(
      mapSet = { it.uriEncode() },
      mapGet = { it.uriDecode() }
    )
  }
}

fun PrimitiveKeyBuilder.string(uriEncoded: Boolean = false): NativePrimitiveKey<String?> =
  nativeKey<String?, PrimitiveKeyValueGetter, PrimitiveKeyValueSetter>(
    get = { getString(name, null) },
    set = { setString(name, it) },
    backingDefault = { null }
  ).let { key ->
    when (uriEncoded) {
      false -> key
      true  -> key.mapType(
        mapSet = { it?.uriEncode() },
        mapGet = { it?.uriDecode() }
      )
    }
  }

fun PrimitiveKeyBuilder.int(default: Int): NativePrimitiveKey<Int> = int { default }
fun PrimitiveKeyBuilder.int(default: () -> Int): NativePrimitiveKey<Int> = nativeKey(
  get = { getInt(name, default()) },
  set = { setInt(name, it) },
  backingDefault = default
)

fun PrimitiveKeyBuilder.int(): PrimitiveKey<Int?, String?> = string().mapType(
  mapGet = { it?.toInt() },
  mapSet = { it?.toString() }
)

private fun String.uriEncode(): String = Uri.encode(this)
private fun String.uriDecode(): String = Uri.decode(this)
