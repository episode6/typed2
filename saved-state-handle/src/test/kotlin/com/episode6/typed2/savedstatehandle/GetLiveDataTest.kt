@file:OptIn(ExperimentalCoroutinesApi::class)

package com.episode6.typed2.savedstatehandle

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import app.cash.turbine.test
import app.cash.turbine.testIn
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNull
import com.episode6.typed2.asAsync
import com.episode6.typed2.bundles.BundleKeyNamespace
import com.episode6.typed2.bundles.RequiredBundleKeyMissing
import com.episode6.typed2.bundles.asRequired
import com.episode6.typed2.int
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

class GetLiveDataTest {

  object Keys : BundleKeyNamespace() {
    val intKey = key("intKey").int(default = 2)
    val nullableIntKey = key("nullableInt").int()
    val requiredInt = key("requiredInt").int().asRequired()
    val asyncRequiredInt = key("asyncRequiredInt").int().asRequired().asAsync()
  }

  @get:Rule val instantExecutor = InstantTaskExecutorRule()

  val dispatcher = StandardTestDispatcher()

  @Before
  fun setup() {
    Dispatchers.setMain(dispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  private val savedStateHandle: SavedStateHandle = mock()

  @Test fun testIntStateFlow() = runTest {
    val backingLiveData: MutableLiveData<Int> = MutableLiveData(Keys.intKey.backingDefault())
    savedStateHandle.stub {
      onGeneric { getLiveData<Int>(any(), anyOrNull()) } doReturn backingLiveData
    }

    launch {
      val result: MutableLiveData<Int> = savedStateHandle.getLiveData(Keys.intKey)

      assertThat(result.value).isEqualTo(2)

      result.asFlow().test {
        assertThat(awaitItem()).isEqualTo(2)

        backingLiveData.value = 10

        assertThat(awaitItem()).isEqualTo(10)
      }

      cancel()
    }
  }

  @Test fun testNullableIntStateFlow() = runTest {
    val backingLiveData: MutableLiveData<String?> = MutableLiveData(Keys.nullableIntKey.backingDefault())
    savedStateHandle.stub {
      onGeneric { getLiveData<String?>(any(), anyOrNull()) } doReturn backingLiveData
    }

    launch {
      val result: MutableLiveData<Int?> = savedStateHandle.getLiveData(Keys.nullableIntKey)

      assertThat(result.value).isNull()

      result.asFlow().test {
        assertThat(awaitItem()).isNull()

        backingLiveData.value = "10"

        assertThat(awaitItem()).isEqualTo(10)
      }

      cancel()
    }
  }

  @Test fun testRequiredIntStateFlow_noValue() = runTest {
    val backingLiveData: MutableLiveData<String?> = MutableLiveData(Keys.requiredInt.backingDefault())
    savedStateHandle.stub {
      onGeneric { getLiveData<String?>(any(), anyOrNull()) } doReturn backingLiveData
    }

    assertThat { savedStateHandle.getLiveData(Keys.requiredInt) }
      .isFailure().hasClass(RequiredBundleKeyMissing::class)
  }

  @Test fun testRequiredIntStateFlow_hasValue() = runTest {
    val backingLiveData: MutableLiveData<String?> = MutableLiveData("5")
    savedStateHandle.stub {
      onGeneric { getLiveData<String?>(any(), anyOrNull()) } doReturn backingLiveData
    }

    launch {
      val result: MutableLiveData<Int> = savedStateHandle.getLiveData(key = Keys.requiredInt)

      assertThat(result.value).isEqualTo(5)

      result.asFlow().test {
        assertThat(awaitItem()).isEqualTo(5)

        backingLiveData.value = "10"

        assertThat(awaitItem()).isEqualTo(10)
      }

      cancel()
    }
  }

  @Test(expected = RequiredBundleKeyMissing::class) // catches exception in othe coroutine
  fun testRequiredAsyncIntStateFlow_noValue() = runTest(UnconfinedTestDispatcher()) {
    val backingLiveData: MutableLiveData<String?> = MutableLiveData(Keys.asyncRequiredInt.backingDefault())
    savedStateHandle.stub {
      onGeneric { getLiveData<String?>(any(), anyOrNull()) } doReturn backingLiveData
    }

    val result = savedStateHandle.getLiveData(this, Keys.asyncRequiredInt)
    assertThat(result.value).isNull()
    result.asFlow().testIn(this)
  }

  @Test fun testRequiredAsyncIntStateFlow_hasValue() = runTest {
    val backingLiveData: MutableLiveData<String?> = MutableLiveData("5")
    savedStateHandle.stub {
      onGeneric { getLiveData<String?>(any(), anyOrNull()) } doReturn backingLiveData
    }

    launch {
      val result: MutableLiveData<Int> = savedStateHandle.getLiveData(this, Keys.asyncRequiredInt)

      result.asFlow().test {
        assertThat(awaitItem()).isEqualTo(5)

        backingLiveData.value = "10"

        assertThat(awaitItem()).isEqualTo(10)
      }

      cancel()
    }
  }
}