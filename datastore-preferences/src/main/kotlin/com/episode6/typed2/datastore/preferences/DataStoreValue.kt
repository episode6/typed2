package com.episode6.typed2.datastore.preferences

/**
 * Wrapper for values emitted by [mutableStateFlow]. Because DataStore can only be read asynchronously,
 * these state flows can't be initialized with a real value; [Uninitialized] distinguishes that startup
 * state from a genuine read of an absent key (which emits [Loaded] with a null value).
 */
public sealed interface DataStoreValue<out T> {
  public data object Uninitialized : DataStoreValue<Nothing>
  public data class Loaded<out T>(public val value: T) : DataStoreValue<T>
}

public val <T> DataStoreValue<T>.valueOrNull: T? get() = (this as? DataStoreValue.Loaded<T>)?.value
