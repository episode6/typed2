@file:OptIn(ExperimentalCoroutinesApi::class)

package com.episode6.typed2.navigation.compose

import androidx.navigation.NavController
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import com.episode6.typed2.set
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class) // need for Uri.encode/decode usage under the hood
class NavigateRoutesTest {

  private val navController: NavController = mock()

  @Test fun navEmptyArgs() {
    navController.navigateTo(EmptyScreen)

    verify(navController).navigate("empty")
  }

  @Test fun navScreenWithRequiredArgs_success() {
    navController.navigateTo(ScreenWithRequiredArgs) {
      set(ScreenWithRequiredArgs.StringArg, "string1")
      set(ScreenWithRequiredArgs.IntArg, 42)
    }

    verify(navController).navigate("required_args/42/string1")
  }

  @Test fun navScreenWithRequiredArgs_fail() {
    assertThat {
      navController.navigateTo(ScreenWithRequiredArgs) {
        set(ScreenWithRequiredArgs.IntArg, 42)
      }
    }.isFailure()
      .isInstanceOf(MissingRequiredArgumentException::class)
      .hasMessage("Missing required argument \"stringArg\" when navigating to screen \"required_args\"")
  }

  @Test fun navScreenWithOptionalArgs_none() {
    navController.navigateTo(ScreenWithOptionalArgs)

    verify(navController).navigate("opt_args")
  }

  @Test fun navScreenWithOptionalArgs_all() {
    navController.navigateTo(ScreenWithOptionalArgs) {
      set(ScreenWithOptionalArgs.DefaultArg, 37)
      set(ScreenWithOptionalArgs.NullArg, "someString")
    }

    verify(navController).navigate("opt_args?nullArg=someString&defaultArg=37")
  }

  @Test fun navScreenWithAllArgTypes_success_all() {
    navController.navigateTo(ScreenWithAllArgTypes) {
      set(ScreenWithAllArgTypes.NullArg, "notNullThisTime")
      set(ScreenWithAllArgTypes.DefaultArg, 65)
      set(ScreenWithAllArgTypes.RequiredArg, "myString")
    }

    verify(navController).navigate("all_args/myString?nullArg=notNullThisTime&defaultArg=65")
  }

  @Test fun navScreenWithAllArgTypes_success_some() {
    navController.navigateTo(ScreenWithAllArgTypes) {
      set(ScreenWithAllArgTypes.DefaultArg, 65)
      set(ScreenWithAllArgTypes.RequiredArg, "myString")
    }

    verify(navController).navigate("all_args/myString?defaultArg=65")
  }

  @Test fun navScreenWithAllArgTypes_fail() {
    assertThat {
      navController.navigateTo(ScreenWithAllArgTypes) {
        set(ScreenWithAllArgTypes.NullArg, "notNullThisTime")
        set(ScreenWithAllArgTypes.DefaultArg, 65)
      }
    }.isFailure()
      .isInstanceOf(MissingRequiredArgumentException::class)
      .hasMessage("Missing required argument \"stringArg\" when navigating to screen \"all_args\"")
  }

  @Test fun navScreenWithAsyncArgTypes_success_all() = runTest {
    val job = navController.launchNavigateTo(ScreenWithAsyncArgTypes, this) {
      set(ScreenWithAsyncArgTypes.NullArg, "notNullThisTime")
      set(ScreenWithAsyncArgTypes.DefaultArg, 65)
      set(ScreenWithAsyncArgTypes.RequiredArg, "myString")
    }
    job.join()

    verify(navController).navigate("all_args/myString?nullArg=notNullThisTime&defaultArg=65")
  }

  @Test fun navScreenWithAsyncArgTypes_success_some() = runTest {
    withContext(UnconfinedTestDispatcher()) {
      navController.navigateTo(ScreenWithAsyncArgTypes) {
        set(ScreenWithAsyncArgTypes.DefaultArg, 65)
        set(ScreenWithAsyncArgTypes.RequiredArg, "myString")
      }
    }

    verify(navController).navigate("all_args/myString?defaultArg=65")
  }

  @Test fun navScreenWithAsyncArgTypes_fail() = runTest {
    assertThat {
      withContext(UnconfinedTestDispatcher()) {
        navController.navigateTo(ScreenWithAsyncArgTypes) {
          set(ScreenWithAsyncArgTypes.NullArg, "notNullThisTime")
          set(ScreenWithAsyncArgTypes.DefaultArg, 65)
        }
      }
    }.isFailure()
      .isInstanceOf(MissingRequiredArgumentException::class)
      .hasMessage("Missing required argument \"stringArg\" when navigating to screen \"all_args\"")
  }
}
