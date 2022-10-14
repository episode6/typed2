package com.episode6.typed2.savedstatehandle

import android.os.Bundle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.episode6.typed2.AsyncKey
import com.episode6.typed2.Key
import com.episode6.typed2.KeyValueGetter
import com.episode6.typed2.KeyValueSetter
import com.episode6.typed2.bundles.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TypedSavedStateHandle(private val delegate: SavedStateHandle) : BundleValueGetter, BundleValueSetter {
  override fun contains(name: String): Boolean = delegate.contains(name)
  override fun getBundle(name: String): Bundle? = delegate[name]
  override fun getInt(name: String, default: Int): Int = delegate[name] ?: default
  override fun getString(name: String, default: String?): String? = delegate[name] ?: default
  override fun setBundle(name: String, value: Bundle?) { delegate[name] = value }
  override fun setInt(name: String, value: Int) { delegate[name] = value }
  override fun setString(name: String, value: String?) { delegate[name] = value }
}


fun SavedStateHandle.typed(): TypedSavedStateHandle = TypedSavedStateHandle(this)

fun <T, BACKED_BY> SavedStateHandle.get(key: BundleKey<T, BACKED_BY>): T = typed().get(key)
fun <T, BACKED_BY> SavedStateHandle.set(key: BundleKey<T, BACKED_BY>, value: T) = typed().set(key, value)
suspend fun <T, BACKED_BY> SavedStateHandle.get(key: AsyncBundleKey<T, BACKED_BY>): T = typed().get(key)
suspend fun <T, BACKED_BY> SavedStateHandle.set(key: AsyncBundleKey<T, BACKED_BY>, value: T) = typed().set(key, value)

fun <T, BACKED_BY> SavedStateHandle.getLiveData(key: Key<T, *, *, BACKED_BY>): MutableLiveData<T> =
  getLiveData(key.name, key.backingDefault())
    .mapMutable(mapGet = key.mapGet, mapSet = key.mapSet)

fun <T, BACKED_BY> SavedStateHandle.getStateFlow(scope: CoroutineScope, key: Key<T, *, *, BACKED_BY>): StateFlow<T> =
  getStateFlow(key.name, key.backingDefault()).run {
    map { key.mapGet(it) }.stateIn(scope, SharingStarted.Eagerly, initialValue = key.mapGet(value))
  }

fun <T, BACKED_BY> SavedStateHandle.getStateFlow(
  scope: CoroutineScope,
  key: AsyncKey<T, *, *, BACKED_BY>,
): StateFlow<T?> = getStateFlow(key.name, key.backingDefault())
    .map { key.mapGet(it) }
    .stateIn(scope, SharingStarted.Eagerly, null)

private fun <BACKED_BY : Any?, T : Any?> MutableLiveData<BACKED_BY>.mapMutable(
  mapGet: (BACKED_BY) -> T,
  mapSet: (T) -> BACKED_BY,
): MutableLiveData<T> {
  val result = object : MediatorLiveData<T>() {
    fun setValueInternal(value: T) {
      super.setValue(value)
    }

    override fun setValue(value: T) {
      super.setValue(value)
      this@mapMutable.value = mapSet(value)
    }
  }
  value?.let { result.setValueInternal(mapGet(it)) }
  result.addSource(this) { result.setValueInternal(mapGet(it)) }
  return result
}
