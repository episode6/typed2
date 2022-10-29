package com.episode6.typed2.gson

import android.os.Bundle
import assertk.Assert
import assertk.assertThat
import assertk.assertions.*
import com.episode6.typed2.RequiredKeyMissingException
import com.episode6.typed2.bundles.BundleKeyNamespace
import com.episode6.typed2.bundles.get
import com.episode6.typed2.bundles.set
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

data class TestData(val name: String)

class JsonSerializedKeyTest {

  object Keys : BundleKeyNamespace() {
    val nullableData = key("testData").gson<TestData>()
    val defaultData = key("withDefault").gson(default = TestData("default"))
    val requiredData = key("required").gson<TestData>().required()
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

    verify(bundle).putString("testData", null)
  }

  @Test fun testNullable_set_obj() {
    bundle.set(Keys.nullableData, TestData("newData"))

    verify(bundle).putString("testData", jsonTestData("newData"))
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

    verify(bundle).putString("withDefault", jsonTestData("newData"))
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

    verify(bundle).putString("required", jsonTestData("newData"))
  }
}

private fun Assert<TestData>.hasName(name: String) = prop(TestData::name).isEqualTo(name)
private fun Bundle.setupTestDataKey(name: String) {
  stub {
    on { containsKey(name) } doReturn true
    on { getString(name, null) } doReturn jsonTestData(name)
  }
}

private fun jsonTestData(name: String): String = "{\"name\":\"$name\"}"
