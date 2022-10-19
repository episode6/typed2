package com.episode6.typed2.kotlinx.serialization.json

import com.episode6.typed2.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

fun <T : Any> PrimitiveKeyBuilder.json(
  default: T,
  serializer: () -> KSerializer<T>,
  json: () -> Json = { Json },
): PrimitiveKey<T, String?> = json(defaultProvider = { default }, serializer, json)

fun <T : Any> PrimitiveKeyBuilder.json(
  defaultProvider: () -> T,
  serializer: () -> KSerializer<T>,
  json: () -> Json = { Json },
): PrimitiveKey<T, String?> = json(serializer, json).withDefault(defaultProvider)

fun <T : Any> PrimitiveKeyBuilder.json(
  serializer: () -> KSerializer<T>,
  json: () -> Json = { Json },
): PrimitiveKey<T?, String?> = string().mapType(
  mapGet = { it?.let { json().decodeFromString(serializer(), it) } },
  mapSet = { it?.let { json().encodeToString(serializer(), it) } }
)
