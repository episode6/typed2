@file:OptIn(ExperimentalCoroutinesApi::class)

package com.episode6.typed2.sampleapp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.*
import com.episode6.typed2.async
import com.episode6.typed2.datastore.preferences.*
import com.episode6.typed2.gson.gson
import com.episode6.typed2.sampleapp.data.RegularAssDataClass
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class DataStoreInstrumentedTest {
  object Keys : DataStoreKeyNamespace() {
    val bool = key("bool").boolean(default = true)
    val nullBool = key("nullBool").boolean()

    val int = key("int").int(default = 42)
    val nullInt = key("nullInt").int()

    val string = key("string").string(default = "default")
    val nullString = key("nullString").string()

    val dataClass = key("dataClass").gson<RegularAssDataClass>().async()
  }

  companion object {
    // a single instance for the whole class; DataStore forbids multiple active instances per file
    private val dataStore: DataStore<Preferences> by lazy {
      PreferenceDataStoreFactory.create {
        InstrumentationRegistry.getInstrumentation().targetContext.preferencesDataStoreFile("DataStoreInstrumentedTest")
      }
    }
  }

  @Before fun setup() = clearStore()
  @After fun tearDown() = clearStore()
  private fun clearStore() = runTest { dataStore.edit { clear() } }

  @Test fun testBool() = runTest {
    assertThat(dataStore.get(Keys.bool)).isTrue()

    dataStore.set(Keys.bool, false)
    val raw = dataStore.data.first()[booleanPreferencesKey("bool")]
    val typed = dataStore.get(Keys.bool)

    assertThat(raw).isNotNull().isFalse()
    assertThat(typed).isFalse()
  }

  @Test fun testNullBool() = runTest {
    assertThat(dataStore.get(Keys.nullBool)).isNull()

    dataStore.set(Keys.nullBool, true)
    val raw = dataStore.data.first()[stringPreferencesKey("nullBool")]
    val typed = dataStore.get(Keys.nullBool)

    assertThat(raw).isEqualTo("true")
    assertThat(typed).isNotNull().isTrue()
  }

  @Test fun testInt() = runTest {
    assertThat(dataStore.get(Keys.int)).isEqualTo(42)

    dataStore.set(Keys.int, 127)
    val raw = dataStore.data.first()[intPreferencesKey("int")]
    val typed = dataStore.get(Keys.int)

    assertThat(raw).isEqualTo(127)
    assertThat(typed).isEqualTo(127)
  }

  @Test fun testNullInt() = runTest {
    assertThat(dataStore.get(Keys.nullInt)).isNull()

    dataStore.set(Keys.nullInt, 52)
    val raw = dataStore.data.first()[stringPreferencesKey("nullInt")]
    val typed = dataStore.get(Keys.nullInt)

    assertThat(raw).isEqualTo("52")
    assertThat(typed).isEqualTo(52)
  }

  @Test fun testString() = runTest {
    assertThat(dataStore.get(Keys.string)).isEqualTo("default")

    dataStore.set(Keys.string, "hi")
    val raw = dataStore.data.first()[stringPreferencesKey("string")]
    val typed = dataStore.get(Keys.string)

    assertThat(raw).isEqualTo("hi")
    assertThat(typed).isEqualTo("hi")
  }

  @Test fun testNullString() = runTest {
    assertThat(dataStore.get(Keys.nullString)).isNull()

    dataStore.set(Keys.nullString, "yo")
    val raw = dataStore.data.first()[stringPreferencesKey("nullString")]
    val typed = dataStore.get(Keys.nullString)

    assertThat(raw).isEqualTo("yo")
    assertThat(typed).isEqualTo("yo")
  }

  @Test fun testGsonKey() = runTest {
    assertThat(dataStore.get(Keys.dataClass)).isNull()

    dataStore.set(Keys.dataClass, RegularAssDataClass("hi"))

    assertThat(dataStore.get(Keys.dataClass)).isEqualTo(RegularAssDataClass("hi"))
  }

  @Test fun testFlow() = runTest {
    dataStore.flow(Keys.string).test(timeout = 10.seconds) {
      assertThat(awaitItem()).isEqualTo("default")

      dataStore.set(Keys.string, "newValue")

      assertThat(awaitItem()).isEqualTo("newValue")
    }
  }

  @Test fun testMutableStateFlow() = runTest {
    launch {
      val stateFlow = dataStore.mutableStateFlow(Keys.string, this + UnconfinedTestDispatcher())

      stateFlow.test(timeout = 10.seconds) {
        assertThat(awaitItem()).isEqualTo(DataStoreValue.Uninitialized)
        assertThat(awaitItem()).isEqualTo(DataStoreValue.Loaded("default"))

        stateFlow.value = DataStoreValue.Loaded("newValue")

        assertThat(awaitItem()).isEqualTo(DataStoreValue.Loaded("newValue"))
      }
      cancel()
    }
  }
}
