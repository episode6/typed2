package com.episode6.typed2

import android.net.Uri
import java.math.BigDecimal

typealias PrimitiveKey<T, BACKED_BY> = Key<T, BACKED_BY, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter>
typealias AsyncPrimitiveKey<T, BACKED_BY> = AsyncKey<T, BACKED_BY, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter>
typealias NativePrimitiveKey<T> = PrimitiveKey<T, T>

interface PrimitiveKeyBuilder : KeyBuilder {
  val stringsShouldBeEncoded: Boolean get() = false
}

fun PrimitiveKeyBuilder.string(default: String): NativePrimitiveKey<String> =
  nativeKey<String, PrimitiveKeyValueGetter, PrimitiveKeyValueSetter>(
    get = { getString(name, default) ?: default },
    set = { setString(name, it) },
    backingDefault = default
  ).let { key ->
    when (stringsShouldBeEncoded) {
      false -> key
      true  -> key.mapType(
        mapSet = { it.uriEncode() },
        mapGet = { it.uriDecode() }
      )
    }
  }

fun PrimitiveKeyBuilder.string(): NativePrimitiveKey<String?> =
  nativeKey<String, PrimitiveKeyValueGetter, PrimitiveKeyValueSetter>(
    get = { getString(name, null) },
    set = { setString(name, it) },
  ).let { key ->
    when (stringsShouldBeEncoded) {
      false -> key
      true  -> key.mapType(
        mapSet = { it?.uriEncode() },
        mapGet = { it?.uriDecode() }
      )
    }
  }

fun PrimitiveKeyBuilder.int(default: Int): NativePrimitiveKey<Int> = nativeKey(
  get = { getInt(name, default) },
  set = { setInt(name, it) },
  backingDefault = default
)

fun PrimitiveKeyBuilder.int(): PrimitiveKey<Int?, String?> = string().mapType(
  mapGet = { it?.toInt() },
  mapSet = { it?.toString() }
)

fun PrimitiveKeyBuilder.double(): PrimitiveKey<Double?, String?> = string().mapType(
  mapGet = { it?.let { BigDecimal(it).toDouble() } },
  mapSet = { it?.toBigDecimal()?.toPlainString() },
)

private fun String.uriEncode(): String = Uri.encode(this)
private fun String.uriDecode(): String = Uri.decode(this)
