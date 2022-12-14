package com.episode6.typed2

import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import assertk.assertThat
import assertk.assertions.*
import com.episode6.typed2.bundles.*
import org.junit.Test
import org.mockito.kotlin.*

class BundleKeysTest {
  private object Keys : BundleKeyNamespace() {
    val binder = key("binder").binder<TestBinder>()
    val booleanArray = key("booleanArray").booleanArray()
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
    val size = key("size").size()
    val sizeF = key("sizeF").sizeF()
    val sparseParcelableArray = key("sparseParcelableArray").sparseParcelableArray<TestParcelable>()
    val stringList = key("stringList").stringList()
    val double = key("double").double(default = 10.0)
    val doubleArray = key("doubleArray").doubleArray()
    val intArray = key("intArray").intArray()
    val longArray = key("longArray").longArray()
    val stringArray = key("stringArray").stringArray()
    val bool = key("bool").boolean(default = true)
    val nullBool = key("nullBool").boolean()
    val float = key("float").float(default = 12.5f)
    val nullFloat = key("nullFloat").float()
    val int = key("int").int(default = 42)
    val nullInt = key("nullInt").int()
    val long = key("long").long(default = 42L)
    val nullLong = key("nullLong").long()
    val string = key("string").string(default = "default")
    val nullString = key("nullString").string()
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
    on { getStringArrayList(any()) } doReturn null
    on { getBoolean(any(), any()) } doAnswer { it.getArgument(1) }
    on { getDouble(any(), any()) } doAnswer { it.getArgument(1) }
    on { getFloat(any(), any()) } doAnswer { it.getArgument(1) }
    on { getInt(any(), any()) } doAnswer { it.getArgument(1) }
    on { getLong(any(), any()) } doAnswer { it.getArgument(1) }
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

  @Test fun testBooleanArray() {
    assertThat(getter.get(Keys.booleanArray)).isNull()
    setter.set(Keys.booleanArray, booleanArrayOf(true, false))

    inOrder(getter, setter) {
      verify(getter).getBooleanArray("booleanArray")
      verify(setter).setBooleanArray("booleanArray", booleanArrayOf(true, false))
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

  @Test fun testSize() {
    val size: Size = mock()

    assertThat(getter.get(Keys.size)).isNull()
    setter.set(Keys.size, size)

    inOrder(getter, setter) {
      verify(getter).getSize("size")
      verify(setter).setSize("size", size)
    }
  }

  @Test fun testSizeF() {
    val sizeF: SizeF = mock()

    assertThat(getter.get(Keys.sizeF)).isNull()
    setter.set(Keys.sizeF, sizeF)

    inOrder(getter, setter) {
      verify(getter).getSizeF("sizeF")
      verify(setter).setSizeF("sizeF", sizeF)
    }
  }

  @Test fun testSparseParcelableArray() {
    val sparseParcelableArray: SparseArray<TestParcelable> = mock()

    assertThat(getter.get(Keys.sparseParcelableArray)).isNull()
    setter.set(Keys.sparseParcelableArray, sparseParcelableArray)

    inOrder(getter, setter) {
      verify(getter).getSparseParcelableArray("sparseParcelableArray", TestParcelable::class)
      verify(setter).setSparseParcelableArray("sparseParcelableArray", sparseParcelableArray)
    }
  }

  @Test fun testStringList() {
    assertThat(getter.get(Keys.stringList)).isNull()
    setter.set(Keys.stringList, listOf("42"))

    inOrder(getter, setter) {
      verify(getter).getStringArrayList("stringList")
      verify(setter).setStringArrayList("stringList", ArrayList(listOf("42")))
    }
  }

  @Test fun testDouble() {
    assertThat(getter.get(Keys.double)).isEqualTo(10.0)
    setter.set(Keys.double, 42.0)

    inOrder(getter, setter) {
      verify(getter).getDouble("double", 10.0)
      verify(setter).setDouble("double", 42.0)
    }
  }

  @Test fun testDoubleArray() {
    assertThat(getter.get(Keys.doubleArray)).isNull()
    setter.set(Keys.doubleArray, doubleArrayOf(99.0, 101.2))

    inOrder(getter, setter) {
      verify(getter).getDoubleArray("doubleArray")
      verify(setter).setDoubleArray("doubleArray", doubleArrayOf(99.0, 101.2))
    }
  }

  @Test fun testIntArray() {
    assertThat(getter.get(Keys.intArray)).isNull()
    setter.set(Keys.intArray, intArrayOf(99, 101))

    inOrder(getter, setter) {
      verify(getter).getIntArray("intArray")
      verify(setter).setIntArray("intArray", intArrayOf(99, 101))
    }
  }

  @Test fun testLongArray() {
    assertThat(getter.get(Keys.longArray)).isNull()
    setter.set(Keys.longArray, longArrayOf(99, 101))

    inOrder(getter, setter) {
      verify(getter).getLongArray("longArray")
      verify(setter).setLongArray("longArray", longArrayOf(99, 101))
    }
  }

  @Test fun testStringArray() {
    assertThat(getter.get(Keys.stringArray)).isNull()
    setter.set(Keys.stringArray, arrayOf("hi", "there"))

    inOrder(getter, setter) {
      verify(getter).getStringArray("stringArray")
      verify(setter).setStringArray("stringArray", arrayOf("hi", "there"))
    }
  }

  @Test fun testBool() {
    assertThat(getter.get(Keys.bool)).isTrue()
    setter.set(Keys.bool, false)

    inOrder(getter, setter) {
      verify(getter).getBoolean("bool", true)
      verify(setter).setBoolean("bool", false)
    }
  }

  @Test fun testNullBool() {
    assertThat(getter.get(Keys.nullBool)).isNull()
    setter.set(Keys.nullBool, false)

    inOrder(getter, setter) {
      verify(getter).getString("nullBool", null)
      verify(setter).setString("nullBool", "false")
    }
  }

  @Test fun testFloat() {
    assertThat(getter.get(Keys.float)).isEqualTo(12.5f)
    setter.set(Keys.float, 127f)

    inOrder(getter, setter) {
      verify(getter).getFloat("float", 12.5f)
      verify(setter).setFloat("float", 127f)
    }
  }

  @Test fun testNullFloat() {
    assertThat(getter.get(Keys.nullFloat)).isNull()
    setter.set(Keys.nullFloat, 52f)

    inOrder(getter, setter) {
      verify(getter).getString("nullFloat", null)
      verify(setter).setString("nullFloat", "52.0")
    }
  }

  @Test fun testInt() {
    assertThat(getter.get(Keys.int)).isEqualTo(42)
    setter.set(Keys.int, 127)

    inOrder(getter, setter) {
      verify(getter).getInt("int", 42)
      verify(setter).setInt("int", 127)
    }
  }

  @Test fun testNullInt() {
    assertThat(getter.get(Keys.nullInt)).isNull()
    setter.set(Keys.nullInt, 52)

    inOrder(getter, setter) {
      verify(getter).getString("nullInt", null)
      verify(setter).setString("nullInt", "52")
    }
  }

  @Test fun testLong() {
    assertThat(getter.get(Keys.long)).isEqualTo(42L)
    setter.set(Keys.long, 127L)

    inOrder(getter, setter) {
      verify(getter).getLong("long", 42L)
      verify(setter).setLong("long", 127L)
    }
  }

  @Test fun testNullLong() {
    assertThat(getter.get(Keys.nullLong)).isNull()
    setter.set(Keys.nullLong, 52)

    inOrder(getter, setter) {
      verify(getter).getString("nullLong", null)
      verify(setter).setString("nullLong", "52")
    }
  }

  @Test fun testString() {
    assertThat(getter.get(Keys.string)).isEqualTo("default")
    setter.set(Keys.string, "hi")

    inOrder(getter, setter) {
      verify(getter).getString("string", "default")
      verify(setter).setString("string", "hi")
    }
  }

  @Test fun testNullString() {
    assertThat(getter.get(Keys.nullString)).isNull()
    setter.set(Keys.nullString, "yo")

    inOrder(getter, setter) {
      verify(getter).getString("nullString", null)
      verify(setter).setString("nullString", "yo")
    }
  }
}

private interface TestBinder: IBinder
private interface TestParcelable : Parcelable
private interface TestSerializable : java.io.Serializable
