package com.episode6.typed2

object EmptyNamespace : KeyNamespace(prefix = "")

open class KeyNamespace(val prefix: String) {
  protected fun key(name: String): KeyBuilder = KeyBuilder(prefix + name)
}

class KeyBuilder internal constructor(val name: String)


fun KeyBuilder.int(defaultValue: Int): PrimitiveKey<Int> = primitiveKey(name, RawType.Int, defaultValue)

fun KeyBuilder.nullableInt(defaultValue: Int? = null): Key<String?, Int?> = Key(
  name = name,
  rawType = RawType.String,
  nullable = true,
  translateFromRaw = { it?.toInt() },
  translateToRaw = { it?.toString() },
  defaultTranslation = defaultValue
)

fun KeyBuilder.string(defaultValue: String): PrimitiveKey<String> = primitiveKey(name, RawType.String, defaultValue)

fun KeyBuilder.nullableString(defaultValue: String? = null): PrimitiveKey<String?> = primitiveKey<String?>(name, RawType.String, defaultValue, nullable = true)

private typealias PrimitiveKey<T> = Key<T, T>

private fun <T: Any?> primitiveKey(
  name: String,
  rawType: RawType,
  defaultValue: T?,
  nullable: Boolean = false,
) = Key<T, T>(
  name = name,
  rawType = rawType,
  nullable = nullable,
  translateToRaw = { it },
  translateFromRaw = { it },
  defaultTranslation = defaultValue,
)
