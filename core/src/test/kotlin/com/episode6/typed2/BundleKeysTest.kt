package com.episode6.typed2

import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
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
    val byteArray = key("byteArray").byteArray()
    val char = key("char").char(default = 'f')
    val nullChar = key("nullChar").char()
    val charArray = key("charArray").charArray(default = charArrayOf())
    val charSequence = key("charSequence").charSequence()
    val charSequenceArray = key("charSequenceArray").charSequenceArray()
    val charSequenceList = key("charSequenceList").charSequenceList()
    val floatArray = key("floatArray").floatArray()
    val intList = key("intList").intList()
    val parcelable = key("parcelable").parcelable<TestParcelable>()
    val parcelableArray = key("parcelableArray").parcelableArray<TestParcelable>()
    val parcelableList = key("parcelableList").parcelableList<TestParcelable>()
    val serializable = key("serializable").serializable<TestSerializable>()
    val short = key("short").short(default = 12)
    val nullShort = key("nullShort").short()
    val shortArray = key("shortArray").shortArray()
  }

  private val getter: BundleValueGetter = mock {
    on { contains(any()) } doReturn false
    on { getByte(any(), any()) } doAnswer { it.getArgument(1) }
    on { getChar(any(), any()) } doAnswer { it.getArgument(1) }
    on { getCharSequenceArrayList(any()) } doReturn null
    on { getIntArrayList(any()) } doReturn null
    on { getParcelableArrayList<TestParcelable>(any(), any()) } doReturn null
    on { getShort(any(), any()) } doAnswer { it.getArgument(1) }
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

  @Test fun testByteArray() {
    assertThat(getter.get(Keys.byteArray)).isNull()
    setter.set(Keys.byteArray, byteArrayOf(99, 101))

    inOrder(getter, setter) {
      verify(getter).getByteArray("byteArray")
      verify(setter).setByteArray("byteArray", byteArrayOf(99, 101))
    }
  }

  @Test fun testChar() {
    assertThat(getter.get(Keys.char)).isEqualTo('f')
    setter.set(Keys.char, 'q')

    inOrder(getter, setter) {
      verify(getter).getChar("char", 'f')
      verify(setter).setChar("char", 'q')
    }
  }

  @Test fun testNullChar() {
    assertThat(getter.get(Keys.nullChar)).isNull()
    setter.set(Keys.nullChar, 't')

    inOrder(getter, setter) {
      verify(getter).getString("nullChar", null)
      verify(setter).setString("nullChar", "t")
    }
  }

  @Test fun testCharArray() {
    assertThat(getter.get(Keys.charArray)).isNotNull().isEmpty()
    setter.set(Keys.charArray, charArrayOf('r', 't'))

    inOrder(getter, setter) {
      verify(getter).contains("charArray")
      verify(setter).setCharArray("charArray", charArrayOf('r', 't'))
    }
  }

  @Test fun testCharSequence() {
    assertThat(getter.get(Keys.charSequence)).isNull()
    setter.set(Keys.charSequence, "hi")

    inOrder(getter, setter) {
      verify(getter).getCharSequence("charSequence")
      verify(setter).setCharSequence("charSequence", "hi")
    }
  }

  @Test fun testCharSequenceArray() {
    assertThat(getter.get(Keys.charSequenceArray)).isNull()
    setter.set(Keys.charSequenceArray, arrayOf("hi"))

    inOrder(getter, setter) {
      verify(getter).getCharSequenceArray("charSequenceArray")
      verify(setter).setCharSequenceArray("charSequenceArray", arrayOf("hi"))
    }
  }

  @Test fun testCharSequenceList() {
    assertThat(getter.get(Keys.charSequenceList)).isNull()
    setter.set(Keys.charSequenceList, listOf("hi"))

    inOrder(getter, setter) {
      verify(getter).getCharSequenceArrayList("charSequenceList")
      verify(setter).setCharSequenceArrayList("charSequenceList", ArrayList(listOf("hi")))
    }
  }

  @Test fun testFloatArray() {
    assertThat(getter.get(Keys.floatArray)).isNull()
    setter.set(Keys.floatArray, floatArrayOf(1.2f))

    inOrder(getter, setter) {
      verify(getter).getFloatArray("floatArray")
      verify(setter).setFloatArray("floatArray", floatArrayOf(1.2f))
    }
  }

  @Test fun testIntList() {
    assertThat(getter.get(Keys.intList)).isNull()
    setter.set(Keys.intList, listOf(42))

    inOrder(getter, setter) {
      verify(getter).getIntArrayList("intList")
      verify(setter).setIntArrayList("intList", ArrayList(listOf(42)))
    }
  }

  @Test fun testParcelable() {
    val mockParcelable = mock<TestParcelable>()

    assertThat(getter.get(Keys.parcelable)).isNull()
    setter.set(Keys.parcelable, mockParcelable)

    inOrder(getter, setter) {
      verify(getter).getParcelable("parcelable", TestParcelable::class)
      verify(setter).setParcelable("parcelable", mockParcelable)
    }
  }

  @Test fun testParcelableArray() {
    val mockParcelable = mock<TestParcelable>()
    val array: Array<TestParcelable> = arrayOf(mockParcelable)

    assertThat(getter.get(Keys.parcelableArray)).isNull()
    setter.set(Keys.parcelableArray, array)

    inOrder(getter, setter) {
      verify(getter).getParcelableArray(eq("parcelableArray"), eq(TestParcelable::class), any())
      verify(setter).setParcelableArray("parcelableArray", array)
    }
  }

  @Test fun testParcelableList() {
    val mockParcelable = mock<TestParcelable>()
    val list: List<TestParcelable> = listOf(mockParcelable)

    assertThat(getter.get(Keys.parcelableList)).isNull()
    setter.set(Keys.parcelableList, list)

    inOrder(getter, setter) {
      verify(getter).getParcelableArrayList(eq("parcelableList"), eq(TestParcelable::class))
      verify(setter).setParcelableArrayList("parcelableList", ArrayList(list))
    }
  }

  @Test fun testSerializable() {
    val mockSerializable = mock<TestSerializable>()

    assertThat(getter.get(Keys.serializable)).isNull()
    setter.set(Keys.serializable, mockSerializable)

    inOrder(getter, setter) {
      verify(getter).getSerializable("serializable", TestSerializable::class)
      verify(setter).setSerializable("serializable", mockSerializable)
    }
  }

  @Test fun testShort() {
    assertThat(getter.get(Keys.short)).isEqualTo(12)
    setter.set(Keys.short, 42)

    inOrder(getter, setter) {
      verify(getter).getShort("short", 12)
      verify(setter).setShort("short", 42)
    }
  }

  @Test fun testNullShort() {
    assertThat(getter.get(Keys.nullShort)).isNull()
    setter.set(Keys.nullShort, 9)

    inOrder(getter, setter) {
      verify(getter).getString("nullShort", null)
      verify(setter).setString("nullShort", "9")
    }
  }

  @Test fun testShortArray() {
    assertThat(getter.get(Keys.shortArray)).isNull()
    setter.set(Keys.shortArray, shortArrayOf(8, 10))

    inOrder(getter, setter) {
      verify(getter).getShortArray("shortArray")
      verify(setter).setShortArray("shortArray", shortArrayOf(8, 10))
    }
  }
}

private interface TestBinder: IBinder
private interface TestParcelable : Parcelable
private interface TestSerializable : java.io.Serializable
