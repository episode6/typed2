package com.episode6.typed2.savedstatehandle

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.episode6.typed2.bundles.BundleKeyNamespace
import com.episode6.typed2.kotlinx.serialization.bundlizer.bundlized
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@kotlinx.serialization.Serializable data class TestData(val name: String)

@RunWith(RobolectricTestRunner::class)
class SavedStateProviderTest {

  object Keys : BundleKeyNamespace() {
    val testData = key("testData").bundlized(TestData::serializer)
  }

  private val savedStateHandle: SavedStateHandle = SavedStateHandle()

  @Test fun testSetSavedStateProvider() {
    savedStateHandle.setSavedStateProvider(Keys.testData) { TestData("Something") }

    savedStateHandle.savedStateProvider().saveState()

    assertThat(savedStateHandle).hasTestDataObjectWithName("Something")
  }
}

private fun Assert<SavedStateHandle>.hasTestDataObjectWithName(name: String) = transform {
  it.get<Bundle>("testData")!!.getString("name")
}.isEqualTo(name)
