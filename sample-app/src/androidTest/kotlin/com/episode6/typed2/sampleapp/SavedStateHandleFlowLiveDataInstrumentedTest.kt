@file:OptIn(ExperimentalCoroutinesApi::class)

package com.episode6.typed2.sampleapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.episode6.typed2.*
import com.episode6.typed2.bundles.*
import com.episode6.typed2.savedstatehandle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.test.*
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class SavedStateHandleFlowLiveDataInstrumentedTest {

  private object Keys : BundleKeyNamespace() {
    val string = key("string").string(default = "default")
    val asyncString = key("asyncString").string(default = "default").async(UnconfinedTestDispatcher())
  }

  @get:Rule val instantExecutor = InstantTaskExecutorRule()

  private val handle = SavedStateHandle()

  @Test fun testStateFlow() = runTest {
    launch {
      val stateFlow = handle.getStateFlow(Keys.string, this, SharingStarted.WhileSubscribed())

      assertThat(stateFlow.value).isEqualTo("default")

      stateFlow.test(timeout = 10.seconds) {
        assertThat(awaitItem()).isEqualTo("default")

        handle.set(Keys.string, "newValue")

        assertThat(awaitItem()).isEqualTo("newValue")
      }
      cancel()
    }
  }

  @Test fun testSharedFlow() = runTest {
    launch {
      handle.getSharedFlow(Keys.asyncString, this, SharingStarted.WhileSubscribed()).test(timeout = 10.seconds) {
        assertThat(awaitItem()).isEqualTo("default")

        handle.set(Keys.asyncString, "newValue")

        assertThat(awaitItem()).isEqualTo("newValue")
      }
      cancel()
    }
  }

  @Test fun testLiveData() = runTest {
    launch {
      val liveData = handle.getLiveData(Keys.string)

      assertThat(liveData.value).isEqualTo("default")

      liveData.asFlow().test(timeout = 10.seconds) {
        assertThat(awaitItem()).isEqualTo("default")

        liveData.value = "newValue"

        assertThat(awaitItem()).isEqualTo("newValue")

        assertThat(handle.get(Keys.string)).isEqualTo("newValue")
      }
      cancel()
    }
  }

  @Test fun testAsyncLiveData() = runTest {
    launch {
      val liveData = handle.getLiveData(Keys.asyncString, this)

      liveData.asFlow().test(timeout = 10.seconds) {
        assertThat(awaitItem()).isEqualTo("default")

        withContext(UnconfinedTestDispatcher()) {
          liveData.value = "newValue"
        }

        assertThat(awaitItem()).isEqualTo("newValue")

        assertThat(handle.get(Keys.asyncString)).isEqualTo("newValue")
      }
      cancel()
    }
  }
}
