@file:OptIn(ExperimentalCoroutinesApi::class)

package com.episode6.typed2.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class DataStoreFlowsTest {

  object Keys : DataStoreKeyNamespace("com.prefix.") {
    val myInt = key("intKey").int(default = 42)
    val myString = key("string").string()
  }

  @get:Rule val tmpFolder = TemporaryFolder()

  private fun TestScope.newDataStore(): DataStore<Preferences> = PreferenceDataStoreFactory.create(scope = backgroundScope) {
    File(tmpFolder.root, "test.preferences_pb")
  }

  @Test fun testFlowEmitsCurrentValue() = runTest {
    val dataStore = newDataStore()

    dataStore.flow(Keys.myInt).test {
      assertThat(awaitItem()).isEqualTo(42)
    }
  }

  @Test fun testFlowEmitsOnEdit() = runTest {
    val dataStore = newDataStore()

    dataStore.flow(Keys.myInt).test {
      assertThat(awaitItem()).isEqualTo(42)

      dataStore.set(Keys.myInt, 24)

      assertThat(awaitItem()).isEqualTo(24)
    }
  }

  @Test fun testFlowIgnoresUnrelatedKeys() = runTest {
    val dataStore = newDataStore()

    dataStore.flow(Keys.myInt).test {
      assertThat(awaitItem()).isEqualTo(42)

      dataStore.set(Keys.myString, "unrelated")
      dataStore.flow(Keys.myString).first { it == "unrelated" }

      expectNoEvents()
    }
  }

  @Test fun testMutableStateFlowHydrates() = runTest {
    val dataStore = newDataStore()
    dataStore.set(Keys.myInt, 24)

    dataStore.mutableStateFlow(Keys.myInt, backgroundScope).test {
      assertThat(awaitItem()).isEqualTo(DataStoreValue.Uninitialized) // state flows always start uninitialized
      assertThat(awaitItem()).isEqualTo(DataStoreValue.Loaded(24))
    }
  }

  @Test fun testMutableStateFlowDistinguishesAbsentKeyFromUninitialized() = runTest {
    val dataStore = newDataStore()

    dataStore.mutableStateFlow(Keys.myString, backgroundScope).test {
      assertThat(awaitItem()).isEqualTo(DataStoreValue.Uninitialized)
      assertThat(awaitItem()).isEqualTo(DataStoreValue.Loaded<String?>(null)) // loaded, but the key is not present
    }
  }

  @Test fun testMutableStateFlowPersistsWrites() = runTest {
    val dataStore = newDataStore()
    val mutableStateFlow = dataStore.mutableStateFlow(Keys.myInt, backgroundScope)

    mutableStateFlow.test {
      assertThat(awaitItem()).isEqualTo(DataStoreValue.Uninitialized)
      assertThat(awaitItem()).isEqualTo(DataStoreValue.Loaded(42))

      mutableStateFlow.value = DataStoreValue.Loaded(24)

      assertThat(awaitItem()).isEqualTo(DataStoreValue.Loaded(24))
      // suspend until the debounced write lands in the backing store (the test times out on failure)
      dataStore.flow(Keys.myInt).first { it == 24 }
    }
  }

  @Test fun testMutableStateFlowLoadedNullRemovesEntry() = runTest {
    val dataStore = newDataStore()
    dataStore.set(Keys.myString, "hello")
    val mutableStateFlow = dataStore.mutableStateFlow(Keys.myString, backgroundScope)

    mutableStateFlow.test {
      assertThat(awaitItem()).isEqualTo(DataStoreValue.Uninitialized)
      assertThat(awaitItem()).isEqualTo(DataStoreValue.Loaded<String?>("hello"))

      mutableStateFlow.value = DataStoreValue.Loaded(null)

      assertThat(awaitItem()).isEqualTo(DataStoreValue.Loaded<String?>(null))
      // suspend until the remove lands in the backing store (the test times out on failure)
      dataStore.data.first { !it.typed().contains(Keys.myString.name) }
    }
  }

  @Test fun testProperty() = runTest {
    val dataStore = newDataStore()
    dataStore.set(Keys.myInt, 24)

    var intProperty: Int? by dataStore.property(Keys.myInt, backgroundScope)

    assertThat(intProperty).isNull() // async properties always start null
    awaitUntil { intProperty == 24 }

    intProperty = 36
    dataStore.flow(Keys.myInt).first { it == 36 }
  }

  @Test fun testPropertySetNullRemovesEntry() = runTest {
    val dataStore = newDataStore()
    dataStore.set(Keys.myString, "hello")

    var stringProperty: String? by dataStore.property(Keys.myString, backgroundScope)
    awaitUntil { stringProperty == "hello" }

    stringProperty = null

    dataStore.data.first { !it.typed().contains(Keys.myString.name) }
  }

  // waits in real time (not virtual test time) because DataStore's file IO completes in real time
  private suspend fun awaitUntil(condition: () -> Boolean) = withContext(Dispatchers.Default) {
    while (!condition()) delay(5)
  }
}
