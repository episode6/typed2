package com.episode6.typed2.bundles

import android.os.Bundle
import com.episode6.typed2.*

typealias BundleKey<T> = Key<T, in BundleValueGetter, in BundleValueSetter>
typealias AsyncBundleKey<T> = AsyncKey<T, in BundleValueGetter, in BundleValueSetter>

interface BundleKeyBuilder : PrimitiveKeyBuilder
open class BundleKeyNamespace(private val prefix: String = "") {
  private class Builder(override val name: String) : BundleKeyBuilder
  protected fun key(name: String): BundleKeyBuilder = Builder(prefix + name)
}

fun BundleKeyBuilder.bundle(default: Bundle): BundleKey<Bundle> = bundle { default }
fun BundleKeyBuilder.bundle(default: () -> Bundle): BundleKey<Bundle> = bundle().asNonNull(default)
fun BundleKeyBuilder.bundle(): BundleKey<Bundle?> = key(
  get = { getBundle(name) },
  set = { setBundle(name, it) }
)

private fun <T : Any?> BundleKeyBuilder.key(
  get: BundleValueGetter.() -> T,
  set: BundleValueSetter.(T) -> Unit,
) = object : Key<T, BundleValueGetter, BundleValueSetter> {
  override val name: String = this@key.name
  override fun get(getter: BundleValueGetter): T = getter.get()
  override fun set(setter: BundleValueSetter, value: T) = setter.set(value)
}
