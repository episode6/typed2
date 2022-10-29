package com.episode6.typed2

import android.os.PersistableBundle
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNull
import com.episode6.typed2.bundles.*
import org.junit.Test
import org.mockito.kotlin.*


class PersistableBundleTest {

  object Keys : PersistableBundleKeyNamespace() {
    val myInt = key("intKey").int(default = 42)
    val myRequiredInt = key("requiredInt").int().required()
    val myDouble = key("double").double(default = 10.0)
    val nullableDouble = key("nullableDouble").double()
    val persistableBundle = key("bundle").persistableBundle()
  }

  val bundle: PersistableBundle = mock()

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
      .hasClass(RequiredKeyMissingException::class)
  }

  @Test fun testRequiredIntPropertyNotPresent() {
    bundle.stub {
      on { containsKey("requiredInt") } doReturn false
      on { getString("requiredInt", null) } doReturn null
    }

    val result: Int by bundle.property(Keys.myRequiredInt)

    assertThat { result }
      .isFailure()
      .hasClass(RequiredKeyMissingException::class)
  }

  @Test fun testRequiredIntIsNull() {
    bundle.stub {
      on { containsKey("requiredInt") } doReturn true
      on { getString("requiredInt", null) } doReturn null
    }

    assertThat { bundle.get(Keys.myRequiredInt) }
      .isFailure()
      .hasClass(RequiredKeyMissingException::class)
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

  @Test fun testGetPersistableBundle_null() {
    bundle.stub {
      on { containsKey("bundle") } doReturn false
      on { getPersistableBundle("bundle") } doReturn null
    }

    val result = bundle.get(Keys.persistableBundle)

    assertThat(result).isNull()
  }

  @Test fun testGetPersistableBundle_nonNull() {
    val innerBundle: PersistableBundle = mock()
    bundle.stub {
      on { containsKey("bundle") } doReturn true
      on { getPersistableBundle("bundle") } doReturn innerBundle
    }

    val result = bundle.get(Keys.persistableBundle)

    assertThat(result).isEqualTo(innerBundle)
  }

  @Test fun testSetPersistableBundle() {
    val innerBundle: PersistableBundle = mock()

    bundle.set(Keys.persistableBundle, innerBundle)

    verify(bundle).putPersistableBundle("bundle", innerBundle)
  }
}

@Suppress("UNUSED_PARAMETER")
private fun suppressWarning(thing: ()->Any) {}
