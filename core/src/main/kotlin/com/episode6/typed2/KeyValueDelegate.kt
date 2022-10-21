package com.episode6.typed2

import kotlin.reflect.KProperty

class KeyValueDelegate<T : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter>(
  private val key: Key<T, *, GETTER, SETTER>,
  private val getter: GETTER,
  private val setter: SETTER,
) {
  operator fun getValue(thisRef: Any?, property: KProperty<*>): T = key.get(getter)
  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = key.set(setter, value)
}
