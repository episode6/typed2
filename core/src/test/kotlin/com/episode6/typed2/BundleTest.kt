package com.episode6.typed2

import android.os.Bundle
import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import com.episode6.typed2.bundles.BundleKeyNamespace
import com.episode6.typed2.bundles.RequiredBundleKeyMissing
import com.episode6.typed2.bundles.get
import com.episode6.typed2.bundles.property
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify


class BundleTest {

  object Keys : BundleKeyNamespace() {
    val myInt = key("intKey").int(default = 42)
    val myRequiredInt = key("requiredInt").int().required()
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
  }
}
