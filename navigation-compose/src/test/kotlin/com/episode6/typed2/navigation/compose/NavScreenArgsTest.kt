package com.episode6.typed2.navigation.compose

import androidx.lifecycle.SavedStateHandle
import assertk.assertThat
import assertk.assertions.*
import com.episode6.typed2.*
import com.episode6.typed2.savedstatehandle.get
import org.junit.Test
import org.mockito.kotlin.mock

class NavScreenArgsTest {
  object TestScreen : NavScreen("testScreen") {
    val nullableIntArg = key("nullableInt").int()
    val intArg = key("intArg").int(default = 42)
    val requiredInt = key("requiredInt").int().required()
    val asyncInt = key("async").int().async()
  }

  val savedStateHandle: SavedStateHandle = mock()

  @Test fun testArgsList() {
    assertThat(TestScreen.args).containsExactly(
      TestScreen.nullableIntArg,
      TestScreen.intArg,
      TestScreen.requiredInt,
      TestScreen.asyncInt,
    )
  }

  @Test fun testNullableInt() {
    val result: Int? = savedStateHandle.get(TestScreen.nullableIntArg)

    assertThat(result).isNull()
  }

  @Test fun testInt() {
    val result: Int = savedStateHandle.get(TestScreen.intArg)

    assertThat(result).isEqualTo(42)
  }

  @Test fun testRequiredInt() {
    assertThat { savedStateHandle.get(TestScreen.requiredInt) }
      .isFailure().hasClass(RequiredNavArgumentMissing::class)
  }
}
