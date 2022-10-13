package com.episode6.typed2

typealias PrimitiveKey<T, BACKED_BY> = Key<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter, BACKED_BY>
typealias AsyncPrimitiveKey<T, BACKED_BY> = AsyncKey<T, in PrimitiveKeyValueGetter, in PrimitiveKeyValueSetter, BACKED_BY>
typealias NativePrimitiveKey<T> = PrimitiveKey<T, T>

interface PrimitiveKeyBuilder : KeyBuilder

fun PrimitiveKeyBuilder.string(default: String): PrimitiveKey<String, String?> = string { default }
fun PrimitiveKeyBuilder.string(default: () -> String): PrimitiveKey<String, String?> = string().asNonNull(default)
fun PrimitiveKeyBuilder.string(): NativePrimitiveKey<String?> = key(
  get = { getString(name, null) },
  set = { setString(name, it) }
)

fun PrimitiveKeyBuilder.int(default: Int): NativePrimitiveKey<Int> = int { default }
fun PrimitiveKeyBuilder.int(default: () -> Int): NativePrimitiveKey<Int> = key(
  get = { getInt(name, default()) },
  set = { setInt(name, it) }
)
fun PrimitiveKeyBuilder.int(): PrimitiveKey<Int?, String?> = string().mapType(
  mapGet = { it?.toInt() },
  mapSet = { it?.toString() }
)

private fun <T : Any?> PrimitiveKeyBuilder.key(
  get: PrimitiveKeyValueGetter.() -> T,
  set: PrimitiveKeyValueSetter.(T) -> Unit,
) = object : Key<T, PrimitiveKeyValueGetter, PrimitiveKeyValueSetter, T> {
  override val name: String = this@key.name
  override fun mapGet(backedBy: T): T = backedBy
  override fun mapSet(value: T): T = value

  override fun getBackingData(getter: PrimitiveKeyValueGetter): T = get.invoke(getter)
  override fun setBackingData(setter: PrimitiveKeyValueSetter, value: T) = set.invoke(setter, value)
}
