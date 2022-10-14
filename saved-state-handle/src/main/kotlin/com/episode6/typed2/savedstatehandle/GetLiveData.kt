package com.episode6.typed2.savedstatehandle

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import com.episode6.typed2.AsyncKey
import com.episode6.typed2.Key
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

fun <T, BACKED_BY> SavedStateHandle.getLiveData(key: Key<T, *, *, BACKED_BY>): MutableLiveData<T> {
  val backingLiveData = getLiveData(key.name, key.backingDefault())
  val result = MutableMediatorLiveData<T>(onNewValue = { backingLiveData.value = key.mapSet(it) })
  backingLiveData.value?.let { result.setValueSkipCallback(key.mapGet(it)) } ?: key.default?.invoke()?.let {  result.setValueSkipCallback(it) }
  result.addSource(backingLiveData) { result.setValueSkipCallback(key.mapGet(it)) }
  return result
}

fun <T, BACKED_BY> SavedStateHandle.getLiveData(
  scope: CoroutineScope,
  key: AsyncKey<T, *, *, BACKED_BY>,
): MutableLiveData<T> {
  val newValues = MutableSharedFlow<T>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
  val backingLiveData = getLiveData(key.name, key.backingDefault())
  val result = MutableMediatorLiveData<T>(onNewValue = { newValues.tryEmit(it) })
  scope.launch {
    newValues.map { key.mapSet(it) }.collectLatest { backingLiveData.value = it }
  }
  scope.launch {
    backingLiveData.asFlow().map { key.mapGet(it) }.collectLatest { result.setValueSkipCallback(it) }
  }
  return result
}

private class MutableMediatorLiveData<T>(private val onNewValue: (T) -> Unit) : MediatorLiveData<T>() {
  override fun setValue(value: T) {
    super.setValue(value)
    onNewValue(value)
  }

  fun setValueSkipCallback(value: T) {
    super.setValue(value)
  }
}
