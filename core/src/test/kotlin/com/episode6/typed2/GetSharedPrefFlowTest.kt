@file:OptIn(ExperimentalCoroutinesApi::class)

package com.episode6.typed2

import android.content.SharedPreferences
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.episode6.typed2.sharedprefs.PrefKeyNamespace
import com.episode6.typed2.sharedprefs.sharedFlow
import com.episode6.typed2.sharedprefs.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.*

class GetSharedPrefFlowTest {

  object Keys : PrefKeyNamespace() {
    val intKey = key("intKey").int(default = 2)
    val nullableIntKey = key("nullableInt").int()
    val asyncKey = key("asyncInt").int().async()
  }

  private val listener = MutableSharedFlow<SharedPreferences.OnSharedPreferenceChangeListener>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
  private val prefs: SharedPreferences = mock {
    on { registerOnSharedPreferenceChangeListener(any()) } doAnswer {
      listener.tryEmit(it.getArgument(0))
      null
    }
  }

  private suspend fun pingListener(key: KeyDescriptor<*, *>) {
    listener.first().onSharedPreferenceChanged(prefs, key.name)
  }

  @Test fun testIntStateFlow() = runTest {
    prefs.stub {
      on { getInt(any(), any()) } doReturnConsecutively listOf(2, 10)
    }

    launch {
      val result: StateFlow<Int> = prefs.stateFlow(Keys.intKey, this, SharingStarted.Eagerly)

      assertThat(result.value).isEqualTo(2)

      result.test {
        assertThat(awaitItem()).isEqualTo(2)

        pingListener(Keys.intKey)

        assertThat(awaitItem()).isEqualTo(10)
      }

      cancel()
    }
  }

  @Test fun testNullableIntStateFlow() = runTest {
    prefs.stub {
      on { getString(any(), anyOrNull()) } doReturnConsecutively listOf(null, "10")
    }

    launch {
      val result: StateFlow<Int?> = prefs.stateFlow(Keys.nullableIntKey, this, SharingStarted.Eagerly)

      assertThat(result.value).isNull()

      result.test {
        assertThat(awaitItem()).isNull()

        pingListener(Keys.nullableIntKey)

        assertThat(awaitItem()).isEqualTo(10)
      }

      cancel()
    }
  }


  @Test fun testRequiredAsyncIntStateFlow_hasValue() = runTest {
    prefs.stub {
      on { getString(any(), anyOrNull()) } doReturnConsecutively listOf("5", "10")
    }

    launch {
      val result: SharedFlow<Int?> = prefs.sharedFlow(Keys.asyncKey, this, SharingStarted.Eagerly)

      result.test {
        assertThat(awaitItem()).isEqualTo(5)

        pingListener(Keys.asyncKey)

        assertThat(awaitItem()).isEqualTo(10)
      }

      cancel()
    }
  }
}
