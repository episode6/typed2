@file:OptIn(ExperimentalCoroutinesApi::class)

package com.episode6.typed2

import android.content.SharedPreferences
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.episode6.typed2.sharedprefs.PrefKeyNamespace
import com.episode6.typed2.sharedprefs.flow
import com.episode6.typed2.sharedprefs.mutableStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.*

class GetSharedPrefFlowTest {

  object Keys : PrefKeyNamespace() {
    val intKey = key("intKey").int(default = 2)
    val nullableIntKey = key("nullableInt").int()
    val asyncKey = key("asyncInt").int().async(UnconfinedTestDispatcher())
    val asyncNonNullKey = key("nonNull").int(default = 42).async(UnconfinedTestDispatcher())
  }

  private val listener =
    MutableSharedFlow<SharedPreferences.OnSharedPreferenceChangeListener>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
  private val editor: SharedPreferences.Editor = mock()
  private val prefs: SharedPreferences = mock {
    on { registerOnSharedPreferenceChangeListener(any()) } doAnswer {
      listener.tryEmit(it.getArgument(0))
      null
    }
    on { edit() } doReturn editor
  }

  private suspend fun pingListener(key: KeyDescriptor<*, *>) {
    listener.first().onSharedPreferenceChanged(prefs, key.name)
  }

  @Test fun testIntFlow() = runTest {
    prefs.stub {
      on { getInt(any(), any()) } doReturnConsecutively listOf(2, 10)
    }

    launch {
      val result: Flow<Int> = prefs.flow(Keys.intKey)

      result.test {
        assertThat(awaitItem()).isEqualTo(2)

        pingListener(Keys.intKey)

        assertThat(awaitItem()).isEqualTo(10)
      }

      cancel()
    }
  }

  @Test fun testIntMutableStateFlow() = runTest {
    prefs.stub {
      on { getInt(any(), any()) } doReturnConsecutively listOf(2)
    }

    launch {
      val result: MutableStateFlow<Int> = prefs.mutableStateFlow(Keys.intKey, this + UnconfinedTestDispatcher())

      result.test {
        assertThat(awaitItem()).isEqualTo(2)

        result.value = 10

        assertThat(awaitItem()).isEqualTo(10)
        verify(editor).putInt("intKey", 10)
      }

      cancel()
    }
  }

  @Test fun testNullableIntFlow() = runTest {
    prefs.stub {
      on { getString(any(), anyOrNull()) } doReturnConsecutively listOf(null, "10")
    }

    launch {
      val result: Flow<Int?> = prefs.flow(Keys.nullableIntKey)

      result.test {
        assertThat(awaitItem()).isNull()

        pingListener(Keys.nullableIntKey)

        assertThat(awaitItem()).isEqualTo(10)
      }

      cancel()
    }
  }


  @Test fun testRequiredAsyncIntFlow_hasValue() = runTest {
    prefs.stub {
      on { getString(any(), anyOrNull()) } doReturnConsecutively listOf("5", "10")
    }

    launch {
      val result: Flow<Int?> = prefs.flow(Keys.asyncKey)

      result.test {
        assertThat(awaitItem()).isEqualTo(5)

        pingListener(Keys.asyncKey)

        assertThat(awaitItem()).isEqualTo(10)
      }

      cancel()
    }
  }

  @Test fun testRequiredAsyncIntMutableStateFlow_hasValue() = runTest {
    prefs.stub {
      on { getString(any(), anyOrNull()) } doReturnConsecutively listOf("5")
    }

    launch {
      val result: MutableStateFlow<Int?> = prefs.mutableStateFlow(Keys.asyncKey, this + UnconfinedTestDispatcher())

      result.test {
        assertThat(awaitItem()).isEqualTo(5)

        result.value = 10

        assertThat(awaitItem()).isEqualTo(10)
        verify(editor).putString("asyncInt", "10")
      }

      cancel()
    }
  }

  @Test fun testRequiredAsyncIntMutableStateFlow_putNull() = runTest {
    prefs.stub {
      on { getInt(any(), any()) } doAnswer {it.getArgument(1)}
    }
    launch {
      val result: MutableStateFlow<Int?> = prefs.mutableStateFlow(Keys.asyncNonNullKey, this + UnconfinedTestDispatcher())

      result.test {
        assertThat(awaitItem()).isEqualTo(42)

        result.value = 10

        assertThat(awaitItem()).isEqualTo(10)
        verify(editor).putInt("nonNull", 10)

        result.value = null

        assertThat(awaitItem()).isEqualTo(42)
        verify(editor).remove("nonNull")
      }

      cancel()
    }
  }
}
