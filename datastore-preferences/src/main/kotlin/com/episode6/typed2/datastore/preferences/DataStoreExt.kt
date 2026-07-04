package com.episode6.typed2.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.episode6.typed2.DelegateProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit as androidxEdit

public suspend fun <T> DataStore<Preferences>.get(key: DataStoreKey<T, *>): T = data.first().typed().get(key)

public suspend fun DataStore<Preferences>.edit(action: suspend TypedMutablePreferences.() -> Unit): TypedPreferences =
  androidxEdit { it.typed().action() }.typed()

public fun DataStore<Preferences>.launchEdit(
  scope: CoroutineScope,
  action: suspend TypedMutablePreferences.() -> Unit,
): Job = scope.launch { edit(action) }

public suspend fun <T> DataStore<Preferences>.set(key: DataStoreKey<T, *>, value: T) {
  edit { set(key, value) }
}

public suspend fun DataStore<Preferences>.remove(key: DataStoreKey<*, *>) {
  edit { remove(key) }
}

public suspend fun <T> DataStore<Preferences>.update(key: DataStoreKey<T, *>, reducer: suspend (T) -> T) {
  edit { set(key, reducer(get(key))) }
}

/**
 * Returns a nullable property delegate backed by [mutableStateFlow]; it reads null until the first
 * value is loaded from the store, and setting null removes the entry.
 */
public fun <T> DataStore<Preferences>.property(key: DataStoreKey<T, *>, scope: CoroutineScope): DelegateProperty<T?> {
  val stateFlow = mutableStateFlow(key, scope)
  return DelegateProperty(
    get = { stateFlow.value.valueOrNull },
    set = { value ->
      if (value == null) launchEdit(scope) { remove(key) } else stateFlow.value = DataStoreValue.Loaded(value)
    },
  )
}
