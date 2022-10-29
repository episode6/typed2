package com.episode6.typed2.kotlinx.serialization.bundlizer

import android.os.Bundle
import assertk.Assert
import assertk.assertThat
import assertk.assertions.*
import com.episode6.typed2.RequiredKeyMissingException
import com.episode6.typed2.bundles.BundleKeyNamespace
import com.episode6.typed2.bundles.get
import com.episode6.typed2.bundles.set
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner

@kotlinx.serialization.Serializable data class TestData(val name: String)

@RunWith(RobolectricTestRunner::class)
class BundlizedKeyTest {

  object Keys : BundleKeyNamespace() {
    val nullableData = key("testData").bundlized(TestData::serializer)
    val defaultData = key("withDefault").bundlized(default = TestData("default"), TestData::serializer)
    val requiredData = key("required").bundlized(TestData::serializer).required()
  }

  private val bundle: Bundle = mock()

  @Test fun testNullable_get_missing() {
    val result: TestData? = bundle.get(Keys.nullableData)

    assertThat(result).isNull()
  }

  @Test fun testNullable_get_present() {
    bundle.setupTestDataKey("testData")

    val result: TestData? = bundle.get(Keys.nullableData)

    assertThat(result).isNotNull().hasName("testData")
  }

  @Test fun testNullable_set_null() {
    bundle.set(Keys.nullableData, null)

    verify(bundle).putBundle("testData", null)
  }

  @Test fun testNullable_set_obj() {
    bundle.set(Keys.nullableData, TestData("newData"))

    argumentCaptor<Bundle>() {
      verify(bundle).putBundle(eq("testData"), capture())
      assertThat(firstValue).hasNameValue("newData")
    }
  }

  @Test fun testDefault_get_missing() {
    val result: TestData = bundle.get(Keys.defaultData)

    assertThat(result).hasName("default")
  }

  @Test fun testDefault_get_present() {
    bundle.setupTestDataKey("withDefault")

    val result: TestData = bundle.get(Keys.defaultData)

    assertThat(result).hasName("withDefault")
  }

  @Test fun testDefault_set_obj() {
    bundle.set(Keys.defaultData, TestData("newData"))

    argumentCaptor<Bundle>() {
      verify(bundle).putBundle(eq("withDefault"), capture())
      assertThat(firstValue).hasNameValue("newData")
    }
  }

  @Test fun testRequired_get_missing() {
    assertThat { bundle.get(Keys.requiredData) }
      .isFailure().hasClass(RequiredKeyMissingException::class)
  }

  @Test fun testRequired_get_present() {
    bundle.setupTestDataKey("required")

    val result: TestData = bundle.get(Keys.requiredData)

    assertThat(result).hasName("required")
  }

  @Test fun testRequired_set_obj() {
    bundle.set(Keys.requiredData, TestData("newData"))

    argumentCaptor<Bundle>() {
      verify(bundle).putBundle(eq("required"), capture())
      assertThat(firstValue).hasNameValue("newData")
    }
  }
}

private fun Assert<TestData>.hasName(name: String) = prop(TestData::name).isEqualTo(name)
private fun Assert<Bundle>.hasNameValue(name: String) = transform { it.getString("name") }.isEqualTo(name)
private fun Bundle.setupTestDataKey(name: String) {
  stub {
    on { containsKey(name) } doReturn true
    on { getBundle(name) } doReturn bundleTestData(name)
  }
}

private fun bundleTestData(name: String): Bundle = Bundle().apply {
  putString("name", name)
}
