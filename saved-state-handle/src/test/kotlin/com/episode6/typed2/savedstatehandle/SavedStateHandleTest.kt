package com.episode6.typed2.savedstatehandle

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNull
import com.episode6.typed2.bundles.BundleKeyNamespace
import com.episode6.typed2.bundles.RequiredBundleKeyMissing
import com.episode6.typed2.bundles.asRequired
import com.episode6.typed2.int
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.*

class SavedStateHandleTest {
  object Keys : BundleKeyNamespace() {
    val intKey = key("intKey").int(default = 2)
    val nullableIntKey = key("nullableInt").int()
    val requiredInt = key("requiredInt").int().asRequired()
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

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test fun testIntStateFlow() = runTest {
    val backingStateFlow: MutableStateFlow<Int> = MutableStateFlow(Keys.intKey.backingDefault())
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

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test fun testNullableIntStateFlow() = runTest {
    val backingStateFlow: MutableStateFlow<String?> = MutableStateFlow(Keys.nullableIntKey.backingDefault())
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

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test fun testRequiredIntStateFlow_noValue() = runTest {
    val backingStateFlow: MutableStateFlow<String?> = MutableStateFlow(Keys.requiredInt.backingDefault())
    savedStateHandle.stub {
      onGeneric { getStateFlow<String?>(any(), anyOrNull()) } doReturn backingStateFlow
    }

    assertThat { savedStateHandle.getStateFlow(this, Keys.requiredInt) }
      .isFailure().hasClass(RequiredBundleKeyMissing::class)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
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
}


