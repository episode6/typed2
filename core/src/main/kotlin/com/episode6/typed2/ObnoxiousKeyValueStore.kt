package com.episode6.typed2

interface ObnoxiousKeyValueGetter {
  fun contains(name: String): Boolean
  fun getInt(name: String, default: Int): Int
  fun getString(name: String, default: String?): String?
}

interface ObnoxiousKeyValueSetter {
  fun setInt(name: String, value: Int)
  fun setString(name: String, value: String?)
}


@Suppress("UNCHECKED_CAST") fun <T : Any?> ObnoxiousKeyValueGetter.get(key: PrimitiveKey<T>): T =
  getObject(key) as T

@Suppress("UNCHECKED_CAST") fun <RAW : Any?, T : Any?> ObnoxiousKeyValueGetter.get(key: TranslateKey<RAW, T>): T {
  if (!contains(key.name)) return key.defaultTranslation as T
  val raw = getObject(key) as RAW?
  return key.translateFromRaw(raw) as T
}

@Suppress("UNCHECKED_CAST")
suspend fun <RAW : Any?, T : Any?> ObnoxiousKeyValueGetter.get(key: AsyncTranslateKey<RAW, T>): T {
  if (!contains(key.name)) return key.defaultTranslation as T
  val raw = getObject(key) as RAW?
  return key.translateFromRaw(raw) as T
}

fun <T : Any?> ObnoxiousKeyValueSetter.set(key: PrimitiveKey<T>, value: T) {
  setObject(key, value)
}

fun <RAW : Any?, T : Any?> ObnoxiousKeyValueSetter.set(key: TranslateKey<RAW, T>, value: T) {
  val raw = key.translateToRaw(value)
  setObject(key, raw)
}

suspend fun <RAW : Any?, T : Any?> ObnoxiousKeyValueSetter.set(key: AsyncTranslateKey<RAW, T>, value: T) {
  val raw = key.translateToRaw(value)
  setObject(key, raw)
}

private fun ObnoxiousKeyValueGetter.getObject(key: KeyData<*>): Any? = with(key) {
  when (key.rawType) {
    is RawType.Int    -> getInt(name, defaultValue as Int)
    is RawType.String -> getString(name, defaultValue as String?)
  }
}

private fun ObnoxiousKeyValueSetter.setObject(key: KeyData<*>, value: Any?): Any? = with(key) {
  when (key.rawType) {
    is RawType.Int    -> setInt(name, value as Int)
    is RawType.String -> setString(name, value as String?)
  }
}
