package com.episode6.typed2.bundles

import android.annotation.TargetApi
import android.os.Build
import android.os.PersistableBundle
import com.episode6.typed2.DelegateProperty
import kotlinx.coroutines.CoroutineScope

class TypedPersistableBundle(private val delegate: PersistableBundle) : PersistableBundleValueGetter, PersistableBundleValueSetter {
  override fun getPersistableBundle(name: String): PersistableBundle? = delegate.getPersistableBundle(name)
  @TargetApi(22)
  override fun getBooleanArray(name: String): BooleanArray? = delegate.getBooleanArray(name)
  override fun contains(name: String): Boolean = delegate.containsKey(name)
  override fun getBoolean(name: String, default: Boolean): Boolean = if (Build.VERSION.SDK_INT >= 22) {
    delegate.getBoolean(name, default)
  } else {
    delegate.getInt(name, default.asInt()).asBoolean()
  }
  override fun getFloat(name: String, default: Float): Float = delegate.getDouble(name, default.toDouble()).toFloat()
  override fun getInt(name: String, default: Int): Int = delegate.getInt(name, default)
  override fun getLong(name: String, default: Long): Long = delegate.getLong(name, default)
  override fun getString(name: String, default: String?): String? = delegate.getString(name, default)
  override fun getDouble(name: String, default: Double): Double = delegate.getDouble(name, default)
  override fun getDoubleArray(name: String): DoubleArray? = delegate.getDoubleArray(name)
  override fun getIntArray(name: String): IntArray? = delegate.getIntArray(name)
  override fun getLongArray(name: String): LongArray? = delegate.getLongArray(name)
  override fun getStringArray(name: String): Array<String>? = delegate.getStringArray(name)
  override fun setPersistableBundle(name: String, value: PersistableBundle?) { delegate.putPersistableBundle(name, value) }
  @TargetApi(22)
  override fun setBooleanArray(name: String, value: BooleanArray?) { delegate.putBooleanArray(name, value) }
  override fun remove(name: String) = delegate.remove(name)
  override fun setBoolean(name: String, value: Boolean) {
    if (Build.VERSION.SDK_INT >= 22) {
      delegate.putBoolean(name, value)
    } else {
      delegate.putInt(name, value.asInt())
    }
  }
  override fun setFloat(name: String, value: Float) { delegate.putDouble(name, value.toDouble()) }
  override fun setLong(name: String, value: Long) { delegate.putLong(name, value) }
  override fun setString(name: String, value: String?) = delegate.putString(name, value)
  override fun setInt(name: String, value: Int) = delegate.putInt(name, value)
  override fun setDouble(name: String, value: Double) { delegate.putDouble(name, value) }
  override fun setDoubleArray(name: String, value: DoubleArray?) { delegate.putDoubleArray(name, value) }
  override fun setIntArray(name: String, value: IntArray?) { delegate.putIntArray(name, value) }
  override fun setLongArray(name: String, value: LongArray?) { delegate.putLongArray(name, value) }
  override fun setStringArray(name: String, value: Array<String>?) { delegate.putStringArray(name, value)}
}

fun PersistableBundle.typed(): TypedPersistableBundle = TypedPersistableBundle(this)

fun <T> PersistableBundle.get(key: PersistableBundleKey<T, *>): T = typed().get(key)
fun <T> PersistableBundle.set(key: PersistableBundleKey<T, *>, value: T) = typed().set(key, value)
fun PersistableBundle.remove(key: PersistableBundleKey<*, *>) = typed().remove(key)
suspend fun <T> PersistableBundle.get(key: AsyncPersistableBundleKey<T, *>): T = typed().get(key)
suspend fun <T> PersistableBundle.set(key: AsyncPersistableBundleKey<T, *>, value: T) = typed().set(key, value)
fun PersistableBundle.remove(key: AsyncPersistableBundleKey<*, *>) = typed().remove(key)

fun <T> TypedPersistableBundle.property(key: PersistableBundleKey<T, *>): DelegateProperty<T> = DelegateProperty(
  get = { get(key) },
  set = { set(key, it) }
)
fun <T> PersistableBundle.property(key: PersistableBundleKey<T, *>): DelegateProperty<T> = typed().property(key)
fun <T> TypedPersistableBundle.property(key: AsyncPersistableBundleKey<T, *>, scope: CoroutineScope): DelegateProperty<T?> =
  DelegateProperty(mutableStateFlow(key, scope))
fun <T> PersistableBundle.property(key: AsyncPersistableBundleKey<T, *>, scope: CoroutineScope): DelegateProperty<T?> = typed().property(key, scope)

inline fun <T> TypedPersistableBundle.update(key: PersistableBundleKey<T, *>, reducer: (T)->T) {
  set(key, reducer(get(key)))
}

suspend inline fun <T> TypedPersistableBundle.update(key: AsyncPersistableBundleKey<T, *>, reducer: (T)->T) {
  set(key, reducer(get(key)))
}

inline fun <T> PersistableBundle.update(key: PersistableBundleKey<T, *>, reducer: (T)->T) = typed().update(key, reducer)
suspend inline fun <T> PersistableBundle.update(key: AsyncPersistableBundleKey<T, *>, reducer: (T)->T) = typed().update(key, reducer)

private fun Int.asBoolean(): Boolean = this > 0
private fun Boolean.asInt(): Int = if (this) 1 else 0
