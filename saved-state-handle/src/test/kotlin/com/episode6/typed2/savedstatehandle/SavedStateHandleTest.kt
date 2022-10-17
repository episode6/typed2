package com.episode6.typed2.savedstatehandle

import androidx.lifecycle.SavedStateHandle
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNull
import com.episode6.typed2.asAsync
import com.episode6.typed2.bundles.BundleKeyNamespace
import com.episode6.typed2.bundles.RequiredBundleKeyMissing
import com.episode6.typed2.int
import org.junit.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

class SavedStateHandleTest {
  object Keys : BundleKeyNamespace() {
    val intKey = key("intKey").int(default = 2)
    val nullableIntKey = key("nullableInt").int()
    val requiredInt = key("requiredInt").int().asRequired()
    val asyncRequiredInt = key("asyncRequiredInt").int().asRequired().asAsync()
  }

  private val savedStateHandle: SavedStateHandle = mock()

  @Test fun testIntKey() {
    savedStateHandle.stub {
      onGeneric { get<Int>("intKey") } doReturn 5
    }

    val result = savedStateHandle.get(Keys.intKey)

    assertThat(result).isEqualTo(5)
  }

  @Test fun testNullableInt() {
    savedStateHandle.stub {
      onGeneric { get<String>("nullableInt") } doAnswer { null }
    }

    val result = savedStateHandle.get(Keys.nullableIntKey)

    assertThat(result).isNull()
  }

  @Test fun testNullableInt_exists() {
    savedStateHandle.stub {
      onGeneric { get<String>("nullableInt") } doReturn "10"
    }

    val result = savedStateHandle.get(Keys.nullableIntKey)

    assertThat(result).isEqualTo(10)
  }

  @Test fun testRequiredInt() {
    savedStateHandle.stub {
      onGeneric { get<String>("requiredInt") } doAnswer { null }
    }

    assertThat { savedStateHandle.get(Keys.requiredInt) }
      .isFailure().hasClass(RequiredBundleKeyMissing::class)
  }
}


