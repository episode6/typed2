package com.episode6.typed2

typealias PrimitiveKey<T> = Key<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter>
typealias AsyncPrimitiveKey<T> = AsyncKey<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter>

interface PrimitiveKeyBuilder : KeyBuilder

fun PrimitiveKeyBuilder.string(default: String): PrimitiveKey<String> = string { default }
fun PrimitiveKeyBuilder.string(default: () -> String): PrimitiveKey<String> = string().asNonNull(default)
fun PrimitiveKeyBuilder.string(): PrimitiveKey<String?> = key(
  get = { getString(name, null) },
  set = { setString(name, it) }
)

fun PrimitiveKeyBuilder.int(default: Int): PrimitiveKey<Int> = int { default }
fun PrimitiveKeyBuilder.int(default: () -> Int): PrimitiveKey<Int> = key(
  get = { getInt(name, default()) },
  set = { setInt(name, it) }
)
fun PrimitiveKeyBuilder.int(): PrimitiveKey<Int?> = string().mapType(
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
