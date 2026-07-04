@file:OptIn(ExperimentalCoroutinesApi::class)

package com.episode6.typed2.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.episode6.typed2.RequiredEnabledKeyNamespace
import com.episode6.typed2.RequiredKeyMissingException
import com.episode6.typed2.async
import com.episode6.typed2.gson.gson
import com.episode6.typed2.mapType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import androidx.datastore.preferences.core.edit as androidxEdit

class DataStoreTest {

  data class TestData(val name: String, val count: Int)

  object Keys : DataStoreKeyNamespace("com.prefix."), RequiredEnabledKeyNamespace {
    val myInt = key("intKey").int(default = 42)
    val myNullInt = key("nullableInt").int()
    val myBool = key("bool").boolean(default = true)
    val myNullBool = key("nullableBool").boolean()
    val myFloat = key("float").float(default = 1.5f)
    val myLong = key("long").long(default = 12L)
    val myString = key("string").string()
    val myDefaultString = key("defaultString").string(default = "default")
    val myRequiredString = key("requiredString").string().required()
    val myDouble = key("double").double()
    val myDefaultDouble = key("defaultDouble").double(default = 2.2)
    val myStringSet = key("stringSet").stringSet()
    val myDefaultStringSet = key("defaultStringSet").stringSet(default = setOf("a"))
    val myMappedInt = key("mappedInt").int().mapType(
      mapGet = { it?.let { TestData(name = "mapped", count = it) } },
      mapSet = { it?.count },
    )
    val myGsonObj = key("gsonObj").gson<TestData>().async()
  }

  @get:Rule val tmpFolder = TemporaryFolder()

  private fun TestScope.newDataStore(): DataStore<Preferences> = PreferenceDataStoreFactory.create(scope = backgroundScope) {
    File(tmpFolder.root, "test.preferences_pb")
  }

  @Test fun testDefaults() = runTest {
    val dataStore = newDataStore()

    assertThat(dataStore.get(Keys.myInt)).isEqualTo(42)
    assertThat(dataStore.get(Keys.myNullInt)).isNull()
    assertThat(dataStore.get(Keys.myBool)).isTrue()
    assertThat(dataStore.get(Keys.myNullBool)).isNull()
    assertThat(dataStore.get(Keys.myFloat)).isEqualTo(1.5f)
    assertThat(dataStore.get(Keys.myLong)).isEqualTo(12L)
    assertThat(dataStore.get(Keys.myString)).isNull()
    assertThat(dataStore.get(Keys.myDefaultString)).isEqualTo("default")
    assertThat(dataStore.get(Keys.myDouble)).isNull()
    assertThat(dataStore.get(Keys.myDefaultDouble)).isEqualTo(2.2)
    assertThat(dataStore.get(Keys.myStringSet)).isNull()
    assertThat(dataStore.get(Keys.myDefaultStringSet)).isEqualTo(setOf("a"))
  }

  @Test fun testRoundTrips() = runTest {
    val dataStore = newDataStore()

    dataStore.edit {
      set(Keys.myInt, 24)
      set(Keys.myNullInt, 112)
      set(Keys.myBool, false)
      set(Keys.myNullBool, true)
      set(Keys.myFloat, 3.5f)
      set(Keys.myLong, 21L)
      set(Keys.myString, "hello")
      set(Keys.myStringSet, setOf("x", "y"))
    }

    assertThat(dataStore.get(Keys.myInt)).isEqualTo(24)
    assertThat(dataStore.get(Keys.myNullInt)).isEqualTo(112)
    assertThat(dataStore.get(Keys.myBool)).isFalse()
    assertThat(dataStore.get(Keys.myNullBool)).isEqualTo(true)
    assertThat(dataStore.get(Keys.myFloat)).isEqualTo(3.5f)
    assertThat(dataStore.get(Keys.myLong)).isEqualTo(21L)
    assertThat(dataStore.get(Keys.myString)).isEqualTo("hello")
    assertThat(dataStore.get(Keys.myStringSet)).isEqualTo(setOf("x", "y"))
  }

  @Test fun testBigDoubleRoundTrip() = runTest {
    val dataStore = newDataStore()

    dataStore.set(Keys.myDouble, 7.9238475894798576E16)

    assertThat(dataStore.get(Keys.myDouble)).isEqualTo(7.9238475894798576E16)
    // doubles are string-backed for consistency with typed2's other key-value stores
    assertThat(dataStore.data.first()[stringPreferencesKey("com.prefix.double")]).isEqualTo("79238475894798576")
  }

  @Test fun testRequiredKey() = runTest {
    val dataStore = newDataStore()

    assertFailure { dataStore.get(Keys.myRequiredString) }.isInstanceOf(RequiredKeyMissingException::class)

    dataStore.set(Keys.myRequiredString, "present")

    assertThat(dataStore.get(Keys.myRequiredString)).isEqualTo("present")
  }

  @Test fun testUpdate() = runTest {
    val dataStore = newDataStore()

    dataStore.update(Keys.myInt) { it + 1 }
    assertThat(dataStore.get(Keys.myInt)).isEqualTo(43) // default + 1

    dataStore.update(Keys.myInt) { it + 1 }
    assertThat(dataStore.get(Keys.myInt)).isEqualTo(44)
  }

  @Test fun testRemove() = runTest {
    val dataStore = newDataStore()
    dataStore.set(Keys.myString, "hello")

    dataStore.remove(Keys.myString)

    assertThat(dataStore.get(Keys.myString)).isNull()
    assertThat(dataStore.data.first().asMap()).isEqualTo(emptyMap())
  }

  @Test fun testRemoveNonStringKey() = runTest {
    val dataStore = newDataStore()
    dataStore.set(Keys.myInt, 24)

    dataStore.remove(Keys.myInt)

    // Preferences.Key equality is name-based, so removal works across value types
    assertThat(dataStore.get(Keys.myInt)).isEqualTo(42)
    assertThat(dataStore.data.first().asMap()).isEqualTo(emptyMap())
  }

  @Test fun testSetNullRemovesEntry() = runTest {
    val dataStore = newDataStore()
    dataStore.set(Keys.myString, "hello")
    dataStore.set(Keys.myStringSet, setOf("x"))

    dataStore.set(Keys.myString, null)
    dataStore.set(Keys.myStringSet, null)

    val prefs = dataStore.data.first().typed()
    assertThat(prefs.contains(Keys.myString.name)).isFalse()
    assertThat(prefs.contains(Keys.myStringSet.name)).isFalse()
  }

  @Test fun testMappedKey() = runTest {
    val dataStore = newDataStore()

    dataStore.set(Keys.myMappedInt, TestData(name = "mapped", count = 7))

    assertThat(dataStore.get(Keys.myMappedInt)).isEqualTo(TestData(name = "mapped", count = 7))
  }

  @Test fun testGsonKey() = runTest {
    val dataStore = newDataStore()

    dataStore.set(Keys.myGsonObj, TestData(name = "json", count = 3))

    assertThat(dataStore.get(Keys.myGsonObj)).isEqualTo(TestData(name = "json", count = 3))
  }

  @Test fun testInteropWithRawDataStore() = runTest {
    val dataStore = newDataStore()

    dataStore.androidxEdit { it[intPreferencesKey("com.prefix.intKey")] = 5 }
    assertThat(dataStore.get(Keys.myInt)).isEqualTo(5)

    dataStore.set(Keys.myInt, 6)
    assertThat(dataStore.data.first()[intPreferencesKey("com.prefix.intKey")]).isEqualTo(6)
  }

  @Test fun testEditReturnsUpdatedPrefs() = runTest {
    val dataStore = newDataStore()

    val result = dataStore.edit { set(Keys.myString, "hello") }

    assertThat(result.get(Keys.myString)).isEqualTo("hello")
  }

  @Test fun testLaunchEdit() = runTest {
    val dataStore = newDataStore()

    dataStore.launchEdit(this) {
      set(Keys.myInt, 343)
      set(Keys.myString, "yahoo")
    }.join()

    assertThat(dataStore.get(Keys.myInt)).isEqualTo(343)
    assertThat(dataStore.get(Keys.myString)).isEqualTo("yahoo")
  }
}
