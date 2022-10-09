package com.episode6.typed2

interface PrimitiveKeyBuilder : KeyBuilder

fun PrimitiveKeyBuilder.nullableString(default: String? = null): PrimitiveKey<String?> = nullableString { default }
fun PrimitiveKeyBuilder.nullableString(default: () -> String?): PrimitiveKey<String?> = primitiveKey(
  get = { getString(name, default()) },
  set = { setString(name, it) }
)

fun PrimitiveKeyBuilder.string(default: String): PrimitiveKey<String> = primitiveKey(
  get = { getString(name, default) ?: default },
  set = { setString(name, it) }
)

fun PrimitiveKeyBuilder.int(default: Int): PrimitiveKey<Int> = primitiveKey(
  get = { getInt(name, default) },
  set = { setInt(name, it) }
)

fun PrimitiveKeyBuilder.nullableInt(default: Int? = null): PrimitiveKey<Int?> =
  nullableString { default?.toString() }.mapType(
    default = default,
    mapGet = { it?.toInt() },
    mapSet = { it?.toString() }
  )

private fun <T : Any?> PrimitiveKeyBuilder.primitiveKey(
  get: PrimitiveKeyValueGetter.() -> T,
  set: PrimitiveKeyValueSetter.(T) -> Unit,
) = object : PrimitiveKey<T> {
  override val name: String = this@primitiveKey.name
  override fun get(getter: PrimitiveKeyValueGetter): T = getter.get()
  override fun set(setter: PrimitiveKeyValueSetter, value: T) = setter.set(value)
}
