package com.episode6.typed2.kotlinx.serialization.bundlizer

import android.os.Bundle
import com.episode6.typed2.bundles.BundleKey
import com.episode6.typed2.bundles.BundleKeyBuilder
import com.episode6.typed2.bundles.bundle
import com.episode6.typed2.mapType
import com.episode6.typed2.withDefault
import dev.ahmedmourad.bundlizer.bundle
import dev.ahmedmourad.bundlizer.unbundle
import kotlinx.serialization.KSerializer

fun <T : Any> BundleKeyBuilder.bundlized(
  default: T,
  serializer: () -> KSerializer<T>,
): BundleKey<T, Bundle?> = bundlized(defaultProvider = { default }, serializer)

fun <T : Any> BundleKeyBuilder.bundlized(
  defaultProvider: () -> T,
  serializer: () -> KSerializer<T>,
): BundleKey<T, Bundle?> = bundlized(serializer).withDefault(defaultProvider)

fun <T : Any> BundleKeyBuilder.bundlized(
  serializer: () -> KSerializer<T>,
): BundleKey<T?, Bundle?> = bundle().mapType(
  mapGet = { it?.unbundle(serializer()) },
  mapSet = { it?.bundle(serializer()) }
)
