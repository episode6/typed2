package com.episode6.typed2.kotlinx.serialization.json

import android.os.Bundle
import com.episode6.typed2.bundles.BundleKeyNamespace
import org.junit.Test
import org.mockito.kotlin.mock

@kotlinx.serialization.Serializable data class TestData(val name: String)

class JsonSerializedKeyTest {

  object Keys : BundleKeyNamespace() {
    val nullableData = key("testData").json(TestData.serializer())
    val defaultData = key("withDefault").json(default = TestData("default"), TestData.serializer())
    val requiredData = key("required").json(TestData.serializer()).required()

    val asyncNullableData = key("testData-async").json(TestData.serializer()).async()
    val asyncDefaultData = key("withDefault-async").json(TestData.serializer()) { TestData("default") }.async()
    val asyncRequiredData = key("required-async").json(TestData.serializer()).required().async()
  }

  private val bundle: Bundle = mock()

  @Test fun testNullable_set() {

  }
}
