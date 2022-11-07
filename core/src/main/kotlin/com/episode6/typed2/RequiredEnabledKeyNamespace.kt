package com.episode6.typed2

interface RequiredEnabledKeyNamespace {
  fun <T : Any, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> Key<T?, BACKED_BY, GETTER, SETTER>.required(
    doesNotExistError: () -> Throwable = { RequiredKeyMissingException(name) }
  ): Key<T, BACKED_BY, GETTER, SETTER> = withOutputDefault(OutputDefault.Required(doesNotExistError))
  fun <T : Any, BACKED_BY : Any?, GETTER : KeyValueGetter, SETTER : KeyValueSetter> AsyncKey<T?, BACKED_BY, GETTER, SETTER>.required(
    doesNotExistError: () -> Throwable = { RequiredKeyMissingException(name) }
  ): AsyncKey<T, BACKED_BY, GETTER, SETTER> = withOutputDefault(AsyncOutputDefault.Required(doesNotExistError))
}

class RequiredKeyMissingException(name: String) : RuntimeException("Required key ($name) missing from typed2 key-value store")
