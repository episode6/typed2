@file:OptIn(ExperimentalCoroutinesApi::class)

package com.episode6.typed2.savedstatehandle

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.testIn
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNull
import com.episode6.typed2.bundles.BundleKeyNamespace
import com.episode6.typed2.bundles.RequiredBundleKeyMissing
import com.episode6.typed2.int
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.*

class GetStateFlowTest {

  object Keys : BundleKeyNamespace() {
    val intKey = key("intKey").int(default = 2)
    val nullableIntKey = key("nullableInt").int()
    val requiredInt = key("requiredInt").int().required()
    val asyncRequiredInt = key("asyncRequiredInt").int().required().async()
  }

  private val savedStateHandle: SavedStateHandle = mock()

  @Test fun testIntStateFlow() = runTest {
    val backingStateFlow: MutableStateFlow<Int> = MutableStateFlow(Keys.intKey.backingTypeInfo.default)
    savedStateHandle.stub {
      onGeneric { getStateFlow<Int>(any(), anyOrNull()) } doReturn backingStateFlow
    }

    launch {
      val result: StateFlow<Int> = savedStateHandle.getStateFlow(this, Keys.intKey)

      assertThat(result.value).isEqualTo(2)

      result.test {
        assertThat(awaitItem()).isEqualTo(2)

        backingStateFlow.emit(10)

        assertThat(awaitItem()).isEqualTo(10)
      }

      cancel()
    }
  }

  @Test fun testNullableIntStateFlow() = runTest {
    val backingStateFlow: MutableStateFlow<String?> = MutableStateFlow(Keys.nullableIntKey.backingTypeInfo.default)
    savedStateHandle.stub {
      onGeneric { getStateFlow<String?>(any(), anyOrNull()) } doReturn backingStateFlow
    }

    launch {
      val result: StateFlow<Int?> = savedStateHandle.getStateFlow(this, Keys.nullableIntKey)

      assertThat(result.value).isNull()

      result.test {
        assertThat(awaitItem()).isNull()

        backingStateFlow.emit("10")

        assertThat(awaitItem()).isEqualTo(10)
      }

      cancel()
    }
  }

  @Test fun testRequiredIntStateFlow_noValue() = runTest {
    val backingStateFlow: MutableStateFlow<String?> = MutableStateFlow(Keys.requiredInt.backingTypeInfo.default)
    savedStateHandle.stub {
      onGeneric { getStateFlow<String?>(any(), anyOrNull()) } doReturn backingStateFlow
    }

    assertThat { savedStateHandle.getStateFlow(this, Keys.requiredInt) }
      .isFailure().hasClass(RequiredBundleKeyMissing::class)
  }

  @Test fun testRequiredIntStateFlow_hasValue() = runTest {
    val backingStateFlow: MutableStateFlow<String?> = MutableStateFlow("5")
    savedStateHandle.stub {
      onGeneric { getStateFlow<String?>(any(), anyOrNull()) } doReturn backingStateFlow
    }

    launch {
      val result: StateFlow<Int?> = savedStateHandle.getStateFlow(this, Keys.requiredInt)

      assertThat(result.value).isEqualTo(5)

      result.test {
        assertThat(awaitItem()).isEqualTo(5)

        backingStateFlow.emit("10")

        assertThat(awaitItem()).isEqualTo(10)
      }

      cancel()
    }
  }

  @Test(expected = RequiredBundleKeyMissing::class) // catches exception in othe coroutine
  fun testRequiredAsyncIntStateFlow_noValue() = runTest(UnconfinedTestDispatcher()) {
    val backingStateFlow: MutableStateFlow<String?> = MutableStateFlow(Keys.asyncRequiredInt.backingTypeInfo.default)
    savedStateHandle.stub {
      onGeneric { getStateFlow<String?>(any(), anyOrNull()) } doReturn backingStateFlow
    }

    val result = savedStateHandle.getStateFlow(this, Keys.asyncRequiredInt)
    assertThat(result.value).isNull()
    result.testIn(this)
  }

  @Test fun testRequiredAsyncIntStateFlow_hasValue() = runTest {
    val backingStateFlow: MutableStateFlow<String?> = MutableStateFlow("5")
    savedStateHandle.stub {
      onGeneric { getStateFlow<String?>(any(), anyOrNull()) } doReturn backingStateFlow
    }

    launch {
      val result: StateFlow<Int?> = savedStateHandle.getStateFlow(this, Keys.asyncRequiredInt)

      result.test {
        assertThat(awaitItem()).isNull()
        assertThat(awaitItem()).isEqualTo(5)

        backingStateFlow.emit("10")

        assertThat(awaitItem()).isEqualTo(10)
      }

      cancel()
    }
  }
}
