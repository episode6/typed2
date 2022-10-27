package com.episode6.typed2

import android.os.Bundle
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNull
import com.episode6.typed2.bundles.*
import org.junit.Test
import org.mockito.kotlin.*


class BundleTest {

  object Keys : BundleKeyNamespace() {
    val myInt = key("intKey").int(default = 42)
    val myRequiredInt = key("requiredInt").int().required()
    val myDouble = key("double").double(default = 10.0)
    val nullableDouble = key("nullableDouble").double()
  }

  val bundle: Bundle = mock()

  @Test fun testRequiredIntPresent() {
    bundle.stub {
      on { containsKey("requiredInt") } doReturn true
      on { getString("requiredInt", null) } doReturn "45"
    }

    val result: Int = bundle.get(Keys.myRequiredInt)

    assertThat(result).isEqualTo(45)
  }

  @Test fun testRequiredIntPropertyPresent() {
    bundle.stub {
      on { containsKey("requiredInt") } doReturn true
      on { getString("requiredInt", null) } doReturn "45"
    }

    val result: Int by bundle.property(Keys.myRequiredInt)

    assertThat(result).isEqualTo(45)
  }

  @Test fun testRequiredIntNotPresent() {
    bundle.stub {
      on { containsKey("requiredInt") } doReturn false
      on { getString("requiredInt", null) } doReturn null
    }

    assertThat { bundle.get(Keys.myRequiredInt) }
      .isFailure()
      .hasClass(RequiredBundleKeyMissing::class)
  }

  @Test fun testRequiredIntPropertyNotPresent() {
    bundle.stub {
      on { containsKey("requiredInt") } doReturn false
      on { getString("requiredInt", null) } doReturn null
    }

    val result: Int by bundle.property(Keys.myRequiredInt)

    assertThat { result }
      .isFailure()
      .hasClass(RequiredBundleKeyMissing::class)
  }

  @Test fun testRequiredIntIsNull() {
    bundle.stub {
      on { containsKey("requiredInt") } doReturn true
      on { getString("requiredInt", null) } doReturn null
    }

    assertThat { bundle.get(Keys.myRequiredInt) }
      .isFailure()
      .hasClass(RequiredBundleKeyMissing::class)
  }


  @Test fun testSetProperty() {
    var result: Int by bundle.property(Keys.myRequiredInt)

    result = 27

    verify(bundle).putString("requiredInt", "27")
    suppressWarning { result }
  }

  @Test fun testGetDouble() {
    bundle.stub {
      on { containsKey("double") } doReturn true
      on { getDouble(eq("double"), any()) } doReturn 19.0
    }

    val result = bundle.get(Keys.myDouble)

    assertThat(result).isEqualTo(19.0)
  }

  @Test fun testGetDouble_default() {
    bundle.stub {
      on { containsKey("double") } doReturn false
      on { getDouble(eq("double"), any()) } doAnswer { it.getArgument(1) }
    }

    val result = bundle.get(Keys.myDouble)

    assertThat(result).isEqualTo(10.0)
  }

  @Test fun testGetNullableDouble() {
    bundle.stub {
      on { containsKey("double") } doReturn false
      on { getString(eq("double"), any()) } doReturn null
    }

    val result = bundle.get(Keys.nullableDouble)

    assertThat(result).isNull()
  }
}

@Suppress("UNUSED_PARAMETER")
private fun suppressWarning(thing: ()->Any) {}
