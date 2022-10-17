package com.episode6.typed2.navigation.compose

import androidx.lifecycle.SavedStateHandle
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isFailure
import com.episode6.typed2.int
import com.episode6.typed2.savedstatehandle.get
import org.junit.Test
import org.mockito.kotlin.mock

class NavScreenArgsTest {
  object TestScreen : NavScreen("testScreen") {
    val nullableIntArg = key("nullableInt").int()
    val intArg = key("intArg").int(default = 42)
    val requiredInt = key("requiredInt").int().asRequired()
  }

  val savedStateHandle: SavedStateHandle = mock()

  @Test fun testNullableInt() {
    val result: Int? = savedStateHandle.get(TestScreen.nullableIntArg)
  }

  @Test fun testInt() {
    val result: Int = savedStateHandle.get(TestScreen.intArg)
  }

  @Test fun testRequiredInt() {
    assertThat { savedStateHandle.get(TestScreen.requiredInt) }
      .isFailure().hasClass(RequiredNavArgumentMissing::class)
  }
}
