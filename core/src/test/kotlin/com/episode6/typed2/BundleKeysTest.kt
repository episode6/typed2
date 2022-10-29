package com.episode6.typed2

import android.os.IBinder
import assertk.assertThat
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
}

private interface TestBinder: IBinder
