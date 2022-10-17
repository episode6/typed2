package com.episode6.typed2.kotlinx.serialization.json

import com.episode6.typed2.PrimitiveKey
import com.episode6.typed2.PrimitiveKeyBuilder
import com.episode6.typed2.mapType
import com.episode6.typed2.string
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json

fun <T : Any, DS> PrimitiveKeyBuilder.json(
  serializer: DS,
  json: Json = Json,
): PrimitiveKey<T?, String?> where DS : SerializationStrategy<T>, DS : DeserializationStrategy<T> = string().mapType(
  mapGet = { it?.let { json.decodeFromString(serializer, it) } },
  mapSet = { it?.let { json.encodeToString(serializer, it) } }
)
