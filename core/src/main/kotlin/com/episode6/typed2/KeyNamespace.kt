package com.episode6.typed2

object EmptyNamespace : KeyNamespace(prefix = "")

open class KeyNamespace(val prefix: String) {
  protected fun key(name: String): KeyBuilder = KeyBuilder(prefix + name)
}

class KeyBuilder internal constructor(val name: String)


fun KeyBuilder.int(defaultValue: Int): PrimitiveKey<Int> =
  PrimitiveKey(name, RawType.Int, nullable = false, defaultValue)

fun KeyBuilder.nullableInt(defaultValue: Int? = null): TranslateKey<String?, Int?> = TranslateKey(
  name = name,
  rawType = RawType.String,
  nullable = true,
  translateFromRaw = { it?.toInt() },
  translateToRaw = { it?.toString() },
  defaultTranslation = defaultValue
)

fun KeyBuilder.string(defaultValue: String): PrimitiveKey<String> =
  PrimitiveKey(name, RawType.String, nullable = false, defaultValue)

fun KeyBuilder.nullableString(defaultValue: String? = null): PrimitiveKey<String?> =
  PrimitiveKey(name, RawType.String, nullable = true, defaultValue)

