package com.episode6.typed2.sampleapp

import android.os.Bundle
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import assertk.assertThat
import assertk.assertions.*
import com.episode6.typed2.*
import com.episode6.typed2.bundles.*
import com.episode6.typed2.sampleapp.data.AndroidParcelable
import com.episode6.typed2.sampleapp.data.JavaIoSerializable
import com.episode6.typed2.savedstatehandle.get
import com.episode6.typed2.savedstatehandle.set
import org.junit.Test

class SavedStateHandleInstrumentedTest {
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

  private val handle = SavedStateHandle()

  @Test fun testBooleanArray() {
    assertThat(handle.get(Keys.booleanArray)).isNull()

    handle.set(Keys.booleanArray, booleanArrayOf(true, false))
    val raw = handle.get<BooleanArray>("booleanArray")
    val typed = handle.get(Keys.booleanArray)

    assertThat(raw?.toList()).isEqualTo(listOf(true, false))
    assertThat(typed?.toList()).isEqualTo(listOf(true, false))
  }

  @Test fun testBundle() {
    val newBundle = bundleOf("nothing" to "hi")
    assertThat(handle.get(Keys.bundle)).isNull()

    handle.set(Keys.bundle, newBundle)
    val raw = handle.get<Bundle>("bundle")
    val typed = handle.get(Keys.bundle)

    assertThat(raw).isEqualTo(newBundle)
    assertThat(typed).isEqualTo(newBundle)
  }

  @Test fun testByte() {
    assertThat(handle.get(Keys.byte)).isEqualTo(3)

    handle.set(Keys.byte, 99)
    val raw = handle.get<Byte>("byte")
    val typed = handle.get(Keys.byte)

    assertThat(raw).isEqualTo(99)
    assertThat(typed).isEqualTo(99)
  }

  @Test fun testNullByte() {
    assertThat(handle.get(Keys.nullByte)).isNull()

    handle.set(Keys.nullByte, 99)
    val raw = handle.get<String>("nullByte")
    val typed = handle.get(Keys.nullByte)

    assertThat(raw).isEqualTo("99")
    assertThat(typed).isEqualTo(99)
  }

  @Test fun testByteArray() {
    assertThat(handle.get(Keys.byteArray)).isNull()

    handle.set(Keys.byteArray, byteArrayOf(99, 101))
    val raw = handle.get<ByteArray>("byteArray")
    val typed = handle.get(Keys.byteArray)

    assertThat(raw?.toList()).isEqualTo(listOf<Byte>(99, 101))
    assertThat(typed?.toList()).isEqualTo(listOf<Byte>(99, 101))
  }

  @Test fun testChar() {
    assertThat(handle.get(Keys.char)).isEqualTo('f')

    handle.set(Keys.char, 'q')
    val raw = handle.get<Char>("char")
    val typed = handle.get(Keys.char)

    assertThat(raw).isEqualTo('q')
    assertThat(typed).isEqualTo('q')
  }

  @Test fun testNullChar() {
    assertThat(handle.get(Keys.nullChar)).isNull()

    handle.set(Keys.nullChar, 't')
    val raw = handle.get<String>("nullChar")
    val typed = handle.get(Keys.nullChar)

    assertThat(raw).isEqualTo("t")
    assertThat(typed).isEqualTo('t')
  }

  @Test fun testCharArray() {
    assertThat(handle.get(Keys.charArray)).isNotNull().isEmpty()
    handle.set(Keys.charArray, charArrayOf('r', 't'))

    val raw = handle.get<CharArray>("charArray")
    val typed = handle.get(Keys.charArray)

    assertThat(raw?.toList()).isEqualTo(listOf('r', 't'))
    assertThat(typed.toList()).isEqualTo(listOf('r', 't'))
  }

  @Test fun testCharSequence() {
    assertThat(handle.get(Keys.charSequence)).isNull()

    handle.set(Keys.charSequence, "hi")
    val raw = handle.get<CharSequence>("charSequence")
    val typed = handle.get(Keys.charSequence)

    assertThat(raw).isEqualTo("hi")
    assertThat(typed).isEqualTo("hi")
  }

  @Test fun testCharSequenceArray() {
    assertThat(handle.get(Keys.charSequenceArray)).isNull()

    handle.set(Keys.charSequenceArray, arrayOf("hi"))
    val raw = handle.get<Array<CharSequence>>("charSequenceArray")
    val typed = handle.get(Keys.charSequenceArray)

    assertThat(raw?.toList()).isEqualTo(listOf("hi"))
    assertThat(typed?.toList()).isEqualTo(listOf("hi"))
  }

  @Test fun testCharSequenceList() {
    assertThat(handle.get(Keys.charSequenceList)).isNull()

    handle.set(Keys.charSequenceList, listOf("hi"))
    val raw = handle.get<ArrayList<CharSequence>>("charSequenceList")
    val typed = handle.get(Keys.charSequenceList)

    assertThat(raw).isEqualTo(listOf("hi"))
    assertThat(typed).isEqualTo(listOf("hi"))
  }

  @Test fun testFloatArray() {
    assertThat(handle.get(Keys.floatArray)).isNull()

    handle.set(Keys.floatArray, floatArrayOf(1.2f))
    val raw = handle.get<FloatArray>("floatArray")
    val typed = handle.get(Keys.floatArray)

    assertThat(raw?.toList()).isEqualTo(listOf(1.2f))
    assertThat(typed?.toList()).isEqualTo(listOf(1.2f))
  }

  @Test fun testIntList() {
    assertThat(handle.get(Keys.intList)).isNull()

    handle.set(Keys.intList, listOf(42))
    val raw = handle.get<ArrayList<Int>>("intList")
    val typed = handle.get(Keys.intList)

    assertThat(raw?.toList()).isEqualTo(listOf(42))
    assertThat(typed?.toList()).isEqualTo(listOf(42))
  }

  @Suppress("DEPRECATION")
  @Test fun testParcelable() {
    val testParcelable = AndroidParcelable("hi")

    assertThat(handle.get(Keys.parcelable)).isNull()
    handle.set(Keys.parcelable, testParcelable)
    val raw = handle.get<AndroidParcelable>("parcelable")
    val typed = handle.get(Keys.parcelable)

    assertThat(raw).isEqualTo(testParcelable)
    assertThat(typed).isEqualTo(testParcelable)
  }

  @Suppress("DEPRECATION")
  @Test fun testParcelableArray() {
    val mockParcelable = AndroidParcelable("hi")
    val array: Array<AndroidParcelable> = arrayOf(mockParcelable)

    assertThat(handle.get(Keys.parcelableArray)).isNull()
    handle.set(Keys.parcelableArray, array)
    val raw = handle.get<Array<Parcelable>>("parcelableArray")
    val typed = handle.get(Keys.parcelableArray)

    assertThat(raw?.toList()).isEqualTo(array.toList())
    assertThat(typed?.toList()).isEqualTo(array.toList())
  }

  @Suppress("DEPRECATION")
  @Test fun testParcelableList() {
    val mockParcelable = AndroidParcelable("hi")
    val list: List<AndroidParcelable> = listOf(mockParcelable)

    assertThat(handle.get(Keys.parcelableList)).isNull()
    handle.set(Keys.parcelableList, list)
    val raw = handle.get<ArrayList<AndroidParcelable>>("parcelableList")
    val typed = handle.get(Keys.parcelableList)

    assertThat(raw).isEqualTo(list)
    assertThat(typed).isEqualTo(list)
  }

  @Suppress("DEPRECATION")
  @Test fun testSerializable() {
    val mockSerializable = JavaIoSerializable("hello")

    assertThat(handle.get(Keys.serializable)).isNull()
    handle.set(Keys.serializable, mockSerializable)
    val raw = handle.get<java.io.Serializable>("serializable")
    val typed = handle.get(Keys.serializable)

    assertThat(raw).isEqualTo(mockSerializable)
    assertThat(typed).isEqualTo(mockSerializable)
  }

  @Test fun testShort() {
    assertThat(handle.get(Keys.short)).isEqualTo(12)

    handle.set(Keys.short, 42)
    val raw = handle.get<Short>("short")
    val typed = handle.get(Keys.short)

    assertThat(raw).isEqualTo(42)
    assertThat(typed).isEqualTo(42)
  }

  @Test fun testNullShort() {
    assertThat(handle.get(Keys.nullShort)).isNull()

    handle.set(Keys.nullShort, 9)
    val raw = handle.get<String>("nullShort")
    val typed = handle.get(Keys.nullShort)

    assertThat(raw).isEqualTo("9")
    assertThat(typed).isEqualTo(9)
  }

  @Test fun testShortArray() {
    assertThat(handle.get(Keys.shortArray)).isNull()

    handle.set(Keys.shortArray, shortArrayOf(8, 10))
    val raw = handle.get<ShortArray>("shortArray")
    val typed = handle.get(Keys.shortArray)

    assertThat(raw?.toList()).isEqualTo(listOf<Short>(8, 10))
    assertThat(typed?.toList()).isEqualTo(listOf<Short>(8, 10))
  }

  @Test fun testSize() {
    val size = Size(2, 4)

    assertThat(handle.get(Keys.size)).isNull()
    handle.set(Keys.size, size)
    val raw = handle.get<Size>("size")
    val typed = handle.get(Keys.size)

    assertThat(raw).isEqualTo(size)
    assertThat(typed).isEqualTo(size)
  }

  @Test fun testSizeF() {
    val sizeF = SizeF(2.5f, 8.6f)

    assertThat(handle.get(Keys.sizeF)).isNull()
    handle.set(Keys.sizeF, sizeF)
    val raw = handle.get<SizeF>("sizeF")
    val typed = handle.get(Keys.sizeF)

    assertThat(raw).isEqualTo(sizeF)
    assertThat(typed).isEqualTo(sizeF)
  }

  @Suppress("DEPRECATION")
  @Test fun testSparseParcelableArray() {
    val sparseParcelableArray: SparseArray<AndroidParcelable> = SparseArray()
    sparseParcelableArray.put(2, AndroidParcelable("heyo"))

    assertThat(handle.get(Keys.sparseParcelableArray)).isNull()
    handle.set(Keys.sparseParcelableArray, sparseParcelableArray)
    val raw = handle.get<SparseArray<AndroidParcelable>>("sparseParcelableArray")
    val typed = handle.get(Keys.sparseParcelableArray)

    assertThat(raw).isEqualTo(sparseParcelableArray)
    assertThat(typed).isEqualTo(sparseParcelableArray)
  }

  @Test fun testStringList() {
    assertThat(handle.get(Keys.stringList)).isNull()

    handle.set(Keys.stringList, listOf("42"))
    val raw = handle.get<ArrayList<String>>("stringList")
    val typed = handle.get(Keys.stringList)

    assertThat(raw).isEqualTo(listOf("42"))
    assertThat(typed).isEqualTo(listOf("42"))
  }

  @Test fun testDouble() {
    assertThat(handle.get(Keys.double)).isEqualTo(10.0)

    handle.set(Keys.double, 42.0)
    val raw = handle.get<Double>("double")
    val typed = handle.get(Keys.double)

    assertThat(raw).isEqualTo(42.0)
    assertThat(typed).isEqualTo(42.0)
  }

  @Test fun testDoubleArray() {
    assertThat(handle.get(Keys.doubleArray)).isNull()

    handle.set(Keys.doubleArray, doubleArrayOf(99.0, 101.2))
    val raw = handle.get<DoubleArray>("doubleArray")
    val typed = handle.get(Keys.doubleArray)

    assertThat(raw?.toList()).isEqualTo(listOf(99.0, 101.2))
    assertThat(typed?.toList()).isEqualTo(listOf(99.0, 101.2))
  }

  @Test fun testIntArray() {
    assertThat(handle.get(Keys.intArray)).isNull()

    handle.set(Keys.intArray, intArrayOf(99, 101))
    val raw = handle.get<IntArray>("intArray")
    val typed = handle.get(Keys.intArray)

    assertThat(raw?.toList()).isEqualTo(listOf(99, 101))
    assertThat(typed?.toList()).isEqualTo(listOf(99, 101))
  }

  @Test fun testLongArray() {
    assertThat(handle.get(Keys.longArray)).isNull()

    handle.set(Keys.longArray, longArrayOf(99, 101))
    val raw = handle.get<LongArray>("longArray")
    val typed = handle.get(Keys.longArray)

    assertThat(raw?.toList()).isEqualTo(listOf(99L, 101L))
    assertThat(typed?.toList()).isEqualTo(listOf(99L, 101L))
  }

  @Test fun testStringArray() {
    assertThat(handle.get(Keys.stringArray)).isNull()

    handle.set(Keys.stringArray, arrayOf("hi", "there"))
    val raw = handle.get<Array<String>>("stringArray")
    val typed = handle.get(Keys.stringArray)

    assertThat(raw?.toList()).isEqualTo(listOf("hi", "there"))
    assertThat(typed?.toList()).isEqualTo(listOf("hi", "there"))
  }

@Test fun testBool() {
  assertThat(handle.get(Keys.bool)).isTrue()

  handle.set(Keys.bool, false)
  val raw = handle.get<Boolean>("bool")
  val typed = handle.get(Keys.bool)

  assertThat(raw).isNotNull().isFalse()
  assertThat(typed).isFalse()
}

  @Test fun testNullBool() {
    assertThat(handle.get(Keys.nullBool)).isNull()

    handle.set(Keys.nullBool, true)
    val raw = handle.get<String>("nullBool")
    val typed = handle.get(Keys.nullBool)

    assertThat(raw).isEqualTo("true")
    assertThat(typed).isNotNull().isTrue()
  }

  @Test fun testFloat() {
    assertThat(handle.get(Keys.float)).isEqualTo(12.5f)

    handle.set(Keys.float, 127f)
    val raw = handle.get<Float>("float")
    val typed = handle.get(Keys.float)

    assertThat(raw).isEqualTo(127f)
    assertThat(typed).isEqualTo(127f)
  }

  @Test fun testNullFloat() {
    assertThat(handle.get(Keys.nullFloat)).isNull()

    handle.set(Keys.nullFloat, 52f)
    val raw = handle.get<String>("nullFloat")
    val typed = handle.get(Keys.nullFloat)

    assertThat(raw).isEqualTo("52.0")
    assertThat(typed).isEqualTo(52f)
  }

  @Test fun testInt() {
    assertThat(handle.get(Keys.int)).isEqualTo(42)

    handle.set(Keys.int, 127)
    val raw = handle.get<Int>("int")
    val typed = handle.get(Keys.int)

    assertThat(raw).isEqualTo(127)
    assertThat(typed).isEqualTo(127)
  }

  @Test fun testNullInt() {
    assertThat(handle.get(Keys.nullInt)).isNull()

    handle.set(Keys.nullInt, 52)
    val raw = handle.get<String>("nullInt")
    val typed = handle.get(Keys.nullInt)

    assertThat(raw).isEqualTo("52")
    assertThat(typed).isEqualTo(52)
  }

  @Test fun testLong() {
    assertThat(handle.get(Keys.long)).isEqualTo(42L)

    handle.set(Keys.long, 127L)
    val raw = handle.get<Long>("long")
    val typed = handle.get(Keys.long)

    assertThat(raw).isEqualTo(127)
    assertThat(typed).isEqualTo(127)
  }

  @Test fun testNullLong() {
    assertThat(handle.get(Keys.nullLong)).isNull()

    handle.set(Keys.nullLong, 52)
    val raw = handle.get<String>("nullLong")
    val typed = handle.get(Keys.nullLong)

    assertThat(raw).isEqualTo("52")
    assertThat(typed).isEqualTo(52)
  }

  @Test fun testString() {
    assertThat(handle.get(Keys.string)).isEqualTo("default")

    handle.set(Keys.string, "hi")
    val raw = handle.get<String>("string")
    val typed = handle.get(Keys.string)

    assertThat(raw).isEqualTo("hi")
    assertThat(typed).isEqualTo("hi")
  }

  @Test fun testNullString() {
    assertThat(handle.get(Keys.nullString)).isNull()

    handle.set(Keys.nullString, "yo")
    val raw = handle.get<String>("nullString")
    val typed = handle.get(Keys.nullString)

    assertThat(raw).isEqualTo("yo")
    assertThat(typed).isEqualTo("yo")
  }
}
