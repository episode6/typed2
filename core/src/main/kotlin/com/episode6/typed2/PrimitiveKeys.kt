package com.episode6.typed2

typealias PrimitiveKey<T> = Key<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter>
typealias AsyncPrimitiveKey<T> = AsyncKey<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter>

interface PrimitiveKeyBuilder : KeyBuilder

fun PrimitiveKeyBuilder.string(default: String): PrimitiveKey<String> = string { default }
fun PrimitiveKeyBuilder.string(default: ()->String): PrimitiveKey<String> = key(
  get = { default().let { getString(name, it) ?: it } },
  set = { setString(name, it) }
)

fun PrimitiveKeyBuilder.nullableString(default: String? = null): PrimitiveKey<String?> = nullableString { default }
fun PrimitiveKeyBuilder.nullableString(default: () -> String?): PrimitiveKey<String?> = key(
  get = { getString(name, default()) },
  set = { setString(name, it) }
)

fun PrimitiveKeyBuilder.int(default: Int): PrimitiveKey<Int> = int { default }
fun PrimitiveKeyBuilder.int(default: ()->Int): PrimitiveKey<Int> = key(
  get = { getInt(name, default()) },
  set = { setInt(name, it) }
)

fun PrimitiveKeyBuilder.nullableInt(default: Int? = null): PrimitiveKey<Int?> = nullableInt { default }
fun PrimitiveKeyBuilder.nullableInt(default: ()->Int?): PrimitiveKey<Int?> =
  nullableString { default()?.toString() }.mapType(
    default = default,
    mapGet = { it?.toInt() },
    mapSet = { it?.toString() }
  )

private fun <T : Any?> PrimitiveKeyBuilder.key(
  get: PrimitiveKeyValueGetter.() -> T,
  set: PrimitiveKeyValueSetter.(T) -> Unit,
) = object : Key<T, PrimitiveKeyValueGetter, PrimitiveKeyValueSetter> {
  override val name: String = this@key.name
  override fun get(getter: PrimitiveKeyValueGetter): T = getter.get()
  override fun set(setter: PrimitiveKeyValueSetter, value: T) = setter.set(value)
}
