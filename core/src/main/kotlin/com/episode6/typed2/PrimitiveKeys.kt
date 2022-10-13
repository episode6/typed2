package com.episode6.typed2

typealias PrimitiveKey<T, BACKED_BY> = Key<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter, BACKED_BY>
typealias AsyncPrimitiveKey<T, BACKED_BY> = AsyncKey<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter, BACKED_BY>

interface PrimitiveKeyBuilder : KeyBuilder

fun PrimitiveKeyBuilder.string(default: String): PrimitiveKey<String, String?> = string { default }
fun PrimitiveKeyBuilder.string(default: () -> String): PrimitiveKey<String, String?> = string().asNonNull(default)
fun PrimitiveKeyBuilder.string(): PrimitiveKey<String?, String?> = key(
  get = { getString(name, null) },
  set = { setString(name, it) }
)

fun PrimitiveKeyBuilder.int(default: Int): PrimitiveKey<Int, Int> = int { default }
fun PrimitiveKeyBuilder.int(default: () -> Int): PrimitiveKey<Int, Int> = key(
  get = { getInt(name, default()) },
  set = { setInt(name, it) }
)
fun PrimitiveKeyBuilder.int(): PrimitiveKey<Int?, String?> = string().mapType(
  mapGet = { it?.toInt() },
  mapSet = { it?.toString() }
)

private fun <T : Any?, BACKED_BY: Any?> PrimitiveKeyBuilder.key(
  get: PrimitiveKeyValueGetter.() -> T,
  set: PrimitiveKeyValueSetter.(T) -> Unit,
) = object : Key<T, PrimitiveKeyValueGetter, PrimitiveKeyValueSetter, BACKED_BY> {
  override val name: String = this@key.name
  override fun get(getter: PrimitiveKeyValueGetter): T = getter.get()
  override fun set(setter: PrimitiveKeyValueSetter, value: T) = setter.set(value)
}
