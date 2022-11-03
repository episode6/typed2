package com.episode6.typed2.sampleapp

import android.os.Bundle
import androidx.core.os.bundleOf
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.episode6.typed2.*
import com.episode6.typed2.bundles.*
import com.episode6.typed2.sampleapp.data.AndroidParcelable
import com.episode6.typed2.sampleapp.data.JavaIoSerializable
import org.junit.Test

class BundleInstrumentedTest {
  private object Keys : BundleKeyNamespace() {
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
    val parcelable = key("parcelable").parcelable<AndroidParcelable>()
    val parcelableArray = key("parcelableArray").parcelableArray<AndroidParcelable>()
    val parcelableList = key("parcelableList").parcelableList<AndroidParcelable>()
    val serializable = key("serializable").serializable<JavaIoSerializable>()
    val short = key("short").short(default = 12)
    val nullShort = key("nullShort").short()
    val shortArray = key("shortArray").shortArray()
    val size = key("size").size()
    val sizeF = key("sizeF").sizeF()
    val sparseParcelableArray = key("sparseParcelableArray").sparseParcelableArray<AndroidParcelable>()
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

  private val bundle = Bundle()

  @Test fun testBooleanArray() {
    assertThat(bundle.get(Keys.booleanArray)).isNull()

    bundle.set(Keys.booleanArray, booleanArrayOf(true, false))
    val raw = bundle.getBooleanArray("booleanArray")
    val typed = bundle.get(Keys.booleanArray)

    assertThat(raw?.toList()).isEqualTo(listOf(true, false))
    assertThat(typed?.toList()).isEqualTo(listOf(true, false))
  }

  @Test fun testBundle() {
    val newBundle = bundleOf("nothing" to "hi")
    assertThat(bundle.get(Keys.bundle)).isNull()

    bundle.set(Keys.bundle, newBundle)
    val raw = bundle.getBundle("bundle")
    val typed = bundle.get(Keys.bundle)

    assertThat(raw).isEqualTo(newBundle)
    assertThat(typed).isEqualTo(newBundle)
  }

  @Test fun testByte() {
    assertThat(bundle.get(Keys.byte)).isEqualTo(3)

    bundle.set(Keys.byte, 99)
    val raw = bundle.getByte("byte", -1)
    val typed = bundle.get(Keys.byte)

    assertThat(raw).isEqualTo(99)
    assertThat(typed).isEqualTo(99)
  }

  @Test fun testNullByte() {
    assertThat(bundle.get(Keys.nullByte)).isNull()

    bundle.set(Keys.nullByte, 99)
    val raw = bundle.getString("nullByte", null)
    val typed = bundle.get(Keys.nullByte)

    assertThat(raw).isEqualTo("99")
    assertThat(typed).isEqualTo(99)
  }

  @Test fun testByteArray() {
    assertThat(bundle.get(Keys.byteArray)).isNull()

    bundle.set(Keys.byteArray, byteArrayOf(99, 101))
    val raw = bundle.getByteArray("byteArray")
    val typed = bundle.get(Keys.byteArray)

    assertThat(raw?.toList()).isEqualTo(listOf<Byte>(99, 101))
    assertThat(typed?.toList()).isEqualTo(listOf<Byte>(99, 101))
  }

  @Test fun testChar() {
    assertThat(bundle.get(Keys.char)).isEqualTo('f')

    bundle.set(Keys.char, 'q')
    val raw = bundle.getChar("char", 'm')
    val typed = bundle.get(Keys.char)

    assertThat(raw).isEqualTo('q')
    assertThat(typed).isEqualTo('q')
  }

//  @Test fun testNullChar() {
//    assertThat(bundle.get(Keys.nullChar)).isNull()
//    bundle.set(Keys.nullChar, 't')
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getString("nullChar", null)
//      verify(bundle).setString("nullChar", "t")
//    }
//  }
//
//  @Test fun testCharArray() {
//    assertThat(bundle.get(Keys.charArray)).isNotNull().isEmpty()
//    bundle.set(Keys.charArray, charArrayOf('r', 't'))
//
//    inOrder(bundle, bundle) {
//      verify(bundle).contains("charArray")
//      verify(bundle).setCharArray("charArray", charArrayOf('r', 't'))
//    }
//  }
//
//  @Test fun testCharSequence() {
//    assertThat(bundle.get(Keys.charSequence)).isNull()
//    bundle.set(Keys.charSequence, "hi")
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getCharSequence("charSequence")
//      verify(bundle).setCharSequence("charSequence", "hi")
//    }
//  }
//
//  @Test fun testCharSequenceArray() {
//    assertThat(bundle.get(Keys.charSequenceArray)).isNull()
//    bundle.set(Keys.charSequenceArray, arrayOf("hi"))
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getCharSequenceArray("charSequenceArray")
//      verify(bundle).setCharSequenceArray("charSequenceArray", arrayOf("hi"))
//    }
//  }
//
//  @Test fun testCharSequenceList() {
//    assertThat(bundle.get(Keys.charSequenceList)).isNull()
//    bundle.set(Keys.charSequenceList, listOf("hi"))
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getCharSequenceArrayList("charSequenceList")
//      verify(bundle).setCharSequenceArrayList("charSequenceList", ArrayList(listOf("hi")))
//    }
//  }
//
//  @Test fun testFloatArray() {
//    assertThat(bundle.get(Keys.floatArray)).isNull()
//    bundle.set(Keys.floatArray, floatArrayOf(1.2f))
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getFloatArray("floatArray")
//      verify(bundle).setFloatArray("floatArray", floatArrayOf(1.2f))
//    }
//  }
//
//  @Test fun testIntList() {
//    assertThat(bundle.get(Keys.intList)).isNull()
//    bundle.set(Keys.intList, listOf(42))
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getIntArrayList("intList")
//      verify(bundle).setIntArrayList("intList", ArrayList(listOf(42)))
//    }
//  }
//
//  @Test fun testParcelable() {
//    val mockParcelable = mock<TestParcelable>()
//
//    assertThat(bundle.get(Keys.parcelable)).isNull()
//    bundle.set(Keys.parcelable, mockParcelable)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getParcelable("parcelable", TestParcelable::class)
//      verify(bundle).setParcelable("parcelable", mockParcelable)
//    }
//  }
//
//  @Test fun testParcelableArray() {
//    val mockParcelable = mock<TestParcelable>()
//    val array: Array<TestParcelable> = arrayOf(mockParcelable)
//
//    assertThat(bundle.get(Keys.parcelableArray)).isNull()
//    bundle.set(Keys.parcelableArray, array)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getParcelableArray(eq("parcelableArray"), eq(TestParcelable::class), any())
//      verify(bundle).setParcelableArray("parcelableArray", array)
//    }
//  }
//
//  @Test fun testParcelableList() {
//    val mockParcelable = mock<TestParcelable>()
//    val list: List<TestParcelable> = listOf(mockParcelable)
//
//    assertThat(bundle.get(Keys.parcelableList)).isNull()
//    bundle.set(Keys.parcelableList, list)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getParcelableArrayList(eq("parcelableList"), eq(TestParcelable::class))
//      verify(bundle).setParcelableArrayList("parcelableList", ArrayList(list))
//    }
//  }
//
//  @Test fun testSerializable() {
//    val mockSerializable = mock<TestSerializable>()
//
//    assertThat(bundle.get(Keys.serializable)).isNull()
//    bundle.set(Keys.serializable, mockSerializable)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getSerializable("serializable", TestSerializable::class)
//      verify(bundle).setSerializable("serializable", mockSerializable)
//    }
//  }
//
//  @Test fun testShort() {
//    assertThat(bundle.get(Keys.short)).isEqualTo(12)
//    bundle.set(Keys.short, 42)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getShort("short", 12)
//      verify(bundle).setShort("short", 42)
//    }
//  }
//
//  @Test fun testNullShort() {
//    assertThat(bundle.get(Keys.nullShort)).isNull()
//    bundle.set(Keys.nullShort, 9)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getString("nullShort", null)
//      verify(bundle).setString("nullShort", "9")
//    }
//  }
//
//  @Test fun testShortArray() {
//    assertThat(bundle.get(Keys.shortArray)).isNull()
//    bundle.set(Keys.shortArray, shortArrayOf(8, 10))
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getShortArray("shortArray")
//      verify(bundle).setShortArray("shortArray", shortArrayOf(8, 10))
//    }
//  }
//
//  @Test fun testSize() {
//    val size: Size = mock()
//
//    assertThat(bundle.get(Keys.size)).isNull()
//    bundle.set(Keys.size, size)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getSize("size")
//      verify(bundle).setSize("size", size)
//    }
//  }
//
//  @Test fun testSizeF() {
//    val sizeF: SizeF = mock()
//
//    assertThat(bundle.get(Keys.sizeF)).isNull()
//    bundle.set(Keys.sizeF, sizeF)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getSizeF("sizeF")
//      verify(bundle).setSizeF("sizeF", sizeF)
//    }
//  }
//
//  @Test fun testSparseParcelableArray() {
//    val sparseParcelableArray: SparseArray<TestParcelable> = mock()
//
//    assertThat(bundle.get(Keys.sparseParcelableArray)).isNull()
//    bundle.set(Keys.sparseParcelableArray, sparseParcelableArray)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getSparseParcelableArray("sparseParcelableArray", TestParcelable::class)
//      verify(bundle).setSparseParcelableArray("sparseParcelableArray", sparseParcelableArray)
//    }
//  }
//
//  @Test fun testStringList() {
//    assertThat(bundle.get(Keys.stringList)).isNull()
//    bundle.set(Keys.stringList, listOf("42"))
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getStringArrayList("stringList")
//      verify(bundle).setStringArrayList("stringList", ArrayList(listOf("42")))
//    }
//  }
//
//  @Test fun testDouble() {
//    assertThat(bundle.get(Keys.double)).isEqualTo(10.0)
//    bundle.set(Keys.double, 42.0)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getDouble("double", 10.0)
//      verify(bundle).setDouble("double", 42.0)
//    }
//  }
//
//  @Test fun testDoubleArray() {
//    assertThat(bundle.get(Keys.doubleArray)).isNull()
//    bundle.set(Keys.doubleArray, doubleArrayOf(99.0, 101.2))
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getDoubleArray("doubleArray")
//      verify(bundle).setDoubleArray("doubleArray", doubleArrayOf(99.0, 101.2))
//    }
//  }
//
//  @Test fun testIntArray() {
//    assertThat(bundle.get(Keys.intArray)).isNull()
//    bundle.set(Keys.intArray, intArrayOf(99, 101))
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getIntArray("intArray")
//      verify(bundle).setIntArray("intArray", intArrayOf(99, 101))
//    }
//  }
//
//  @Test fun testLongArray() {
//    assertThat(bundle.get(Keys.longArray)).isNull()
//    bundle.set(Keys.longArray, longArrayOf(99, 101))
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getLongArray("longArray")
//      verify(bundle).setLongArray("longArray", longArrayOf(99, 101))
//    }
//  }
//
//  @Test fun testStringArray() {
//    assertThat(bundle.get(Keys.stringArray)).isNull()
//    bundle.set(Keys.stringArray, arrayOf("hi", "there"))
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getStringArray("stringArray")
//      verify(bundle).setStringArray("stringArray", arrayOf("hi", "there"))
//    }
//  }
//
//  @Test fun testBool() {
//    assertThat(bundle.get(Keys.bool)).isTrue()
//    bundle.set(Keys.bool, false)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getBoolean("bool", true)
//      verify(bundle).setBoolean("bool", false)
//    }
//  }
//
//  @Test fun testNullBool() {
//    assertThat(bundle.get(Keys.nullBool)).isNull()
//    bundle.set(Keys.nullBool, false)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getString("nullBool", null)
//      verify(bundle).setString("nullBool", "false")
//    }
//  }
//
//  @Test fun testFloat() {
//    assertThat(bundle.get(Keys.float)).isEqualTo(12.5f)
//    bundle.set(Keys.float, 127f)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getFloat("float", 12.5f)
//      verify(bundle).setFloat("float", 127f)
//    }
//  }
//
//  @Test fun testNullFloat() {
//    assertThat(bundle.get(Keys.nullFloat)).isNull()
//    bundle.set(Keys.nullFloat, 52f)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getString("nullFloat", null)
//      verify(bundle).setString("nullFloat", "52.0")
//    }
//  }
//
//  @Test fun testInt() {
//    assertThat(bundle.get(Keys.int)).isEqualTo(42)
//    bundle.set(Keys.int, 127)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getInt("int", 42)
//      verify(bundle).setInt("int", 127)
//    }
//  }
//
//  @Test fun testNullInt() {
//    assertThat(bundle.get(Keys.nullInt)).isNull()
//    bundle.set(Keys.nullInt, 52)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getString("nullInt", null)
//      verify(bundle).setString("nullInt", "52")
//    }
//  }
//
//  @Test fun testLong() {
//    assertThat(bundle.get(Keys.long)).isEqualTo(42L)
//    bundle.set(Keys.long, 127L)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getLong("long", 42L)
//      verify(bundle).setLong("long", 127L)
//    }
//  }
//
//  @Test fun testNullLong() {
//    assertThat(bundle.get(Keys.nullLong)).isNull()
//    bundle.set(Keys.nullLong, 52)
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getString("nullLong", null)
//      verify(bundle).setString("nullLong", "52")
//    }
//  }
//
//  @Test fun testString() {
//    assertThat(bundle.get(Keys.string)).isEqualTo("default")
//    bundle.set(Keys.string, "hi")
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getString("string", "default")
//      verify(bundle).setString("string", "hi")
//    }
//  }
//
//  @Test fun testNullString() {
//    assertThat(bundle.get(Keys.nullString)).isNull()
//    bundle.set(Keys.nullString, "yo")
//
//    inOrder(bundle, bundle) {
//      verify(bundle).getString("nullString", null)
//      verify(bundle).setString("nullString", "yo")
//    }
//  }
}
