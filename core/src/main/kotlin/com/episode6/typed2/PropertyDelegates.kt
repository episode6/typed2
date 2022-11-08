package com.episode6.typed2

import kotlin.reflect.KProperty

class PropertyDelegate<T : Any?>(val get: () -> T, val set: (T) -> Unit) {
  operator fun getValue(thisRef: Any?, property: KProperty<*>): T = get()
  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = set(value)
}
