package com.episode6.typed2

import android.os.Bundle
import android.os.IBinder
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.episode6.typed2.bundles.*
import org.junit.Test
import org.mockito.kotlin.*

class BundleKeysTest {
  private object Keys : BundleKeyNamespace() {
    val binder = key("binder").binder<TestBinder>()
    val bundle = key("bundle").bundle()
    val byte = key("byte").byte(default = 3)
    val nullByte = key("nullByte").byte()
  }

  private val getter: BundleValueGetter = mock {
    on { contains(any()) } doReturn false
    on { getByte(any(), any()) } doAnswer { it.getArgument(1) }
    on { getString(any(), anyOrNull()) } doAnswer { it.getArgument(1) }
  }

  private val setter: BundleValueSetter = mock()

  @Test fun testBinder() {
    val mockBinder: TestBinder = mock()

    assertThat(getter.get(Keys.binder)).isNull()
    setter.set(Keys.binder, mockBinder)

    inOrder(getter, setter) {
      verify(getter).getBinder("binder")
      verify(setter).setBinder("binder", mockBinder)
    }
  }

  @Test fun testBundle() {
    val mockBundle: Bundle = mock()

    assertThat(getter.get(Keys.bundle)).isNull()
    setter.set(Keys.bundle, mockBundle)

    inOrder(getter, setter) {
      verify(getter).getBundle("bundle")
      verify(setter).setBundle("bundle", mockBundle)
    }
  }

  @Test fun testByte() {
    assertThat(getter.get(Keys.byte)).isEqualTo(3)
    setter.set(Keys.byte, 99)

    inOrder(getter, setter) {
      verify(getter).getByte("byte", 3)
      verify(setter).setByte("byte", 99)
    }
  }

  @Test fun testNullByte() {
    assertThat(getter.get(Keys.nullByte)).isNull()
    setter.set(Keys.nullByte, 99)

    inOrder(getter, setter) {
      verify(getter).getString("nullByte", null)
      verify(setter).setString("nullByte", "99")
    }
  }
}

private interface TestBinder: IBinder
