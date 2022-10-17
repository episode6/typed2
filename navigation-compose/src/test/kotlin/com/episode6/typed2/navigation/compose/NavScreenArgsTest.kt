package com.episode6.typed2.navigation.compose

import androidx.lifecycle.SavedStateHandle
import com.episode6.typed2.int
import com.episode6.typed2.savedstatehandle.get
import org.junit.Test
import org.mockito.kotlin.mock

class NavScreenArgsTest {
  object TestScreen : NavScreen("testScreen") {
    val nullableIntArg = arg("nullableInt") { int() }
    val intArg = arg("intArg") { int(default = 42) }
    val requiredInt = arg("requiredInt") { int().asRequired() }
  }

  val savedStateHandle: SavedStateHandle = mock()

  @Test fun testNullableInt() {
    val result: Int? = savedStateHandle.get(TestScreen.nullableIntArg)
  }

  @Test fun testInt() {
    val result: Int = savedStateHandle.get(TestScreen.intArg)
  }

  @Test fun testRequiredInt() {
    val result: Int = savedStateHandle.get(TestScreen.requiredInt)
  }
}
