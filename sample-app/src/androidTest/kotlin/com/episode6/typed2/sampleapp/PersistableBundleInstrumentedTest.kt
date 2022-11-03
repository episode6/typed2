package com.episode6.typed2.sampleapp

import android.os.PersistableBundle
import assertk.assertThat
import assertk.assertions.*
import com.episode6.typed2.*
import com.episode6.typed2.bundles.*
import org.junit.Test

class PersistableBundleInstrumentedTest {
  private object Keys : PersistableBundleKeyNamespace() {
    val booleanArray = key("booleanArray").booleanArray()
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
    val persistableBundle = key("persistableBundle").persistableBundle()
  }

  private val bundle = PersistableBundle()

  @Test fun testBooleanArray() {
    assertThat(bundle.get(Keys.booleanArray)).isNull()

    bundle.set(Keys.booleanArray, booleanArrayOf(true, false))
    val raw = bundle.getBooleanArray("booleanArray")
    val typed = bundle.get(Keys.booleanArray)

    assertThat(raw?.toList()).isEqualTo(listOf(true, false))
    assertThat(typed?.toList()).isEqualTo(listOf(true, false))
  }

  @Test fun testDouble() {
    assertThat(bundle.get(Keys.double)).isEqualTo(10.0)

    bundle.set(Keys.double, 42.0)
    val raw = bundle.getDouble("double")
    val typed = bundle.get(Keys.double)

    assertThat(raw).isEqualTo(42.0)
    assertThat(typed).isEqualTo(42.0)
  }

  @Test fun testDoubleArray() {
    assertThat(bundle.get(Keys.doubleArray)).isNull()

    bundle.set(Keys.doubleArray, doubleArrayOf(99.0, 101.2))
    val raw = bundle.getDoubleArray("doubleArray")
    val typed = bundle.get(Keys.doubleArray)

    assertThat(raw?.toList()).isEqualTo(listOf(99.0, 101.2))
    assertThat(typed?.toList()).isEqualTo(listOf(99.0, 101.2))
  }

  @Test fun testIntArray() {
    assertThat(bundle.get(Keys.intArray)).isNull()

    bundle.set(Keys.intArray, intArrayOf(99, 101))
    val raw = bundle.getIntArray("intArray")
    val typed = bundle.get(Keys.intArray)

    assertThat(raw?.toList()).isEqualTo(listOf(99, 101))
    assertThat(typed?.toList()).isEqualTo(listOf(99, 101))
  }

  @Test fun testLongArray() {
    assertThat(bundle.get(Keys.longArray)).isNull()

    bundle.set(Keys.longArray, longArrayOf(99, 101))
    val raw = bundle.getLongArray("longArray")
    val typed = bundle.get(Keys.longArray)

    assertThat(raw?.toList()).isEqualTo(listOf(99L, 101L))
    assertThat(typed?.toList()).isEqualTo(listOf(99L, 101L))
  }

  @Test fun testStringArray() {
    assertThat(bundle.get(Keys.stringArray)).isNull()

    bundle.set(Keys.stringArray, arrayOf("hi", "there"))
    val raw = bundle.getStringArray("stringArray")
    val typed = bundle.get(Keys.stringArray)

    assertThat(raw?.toList()).isEqualTo(listOf("hi", "there"))
    assertThat(typed?.toList()).isEqualTo(listOf("hi", "there"))
  }

  @Test fun testBool() {
    assertThat(bundle.get(Keys.bool)).isTrue()

    bundle.set(Keys.bool, false)
    val raw = bundle.getBoolean("bool", true)
    val typed = bundle.get(Keys.bool)

    assertThat(raw).isFalse()
    assertThat(typed).isFalse()
  }

  @Test fun testNullBool() {
    assertThat(bundle.get(Keys.nullBool)).isNull()

    bundle.set(Keys.nullBool, true)
    val raw = bundle.getString("nullBool", null)
    val typed = bundle.get(Keys.nullBool)

    assertThat(raw).isEqualTo("true")
    assertThat(typed).isNotNull().isTrue()
  }

  @Test fun testFloat() {
    assertThat(bundle.get(Keys.float)).isEqualTo(12.5f)

    bundle.set(Keys.float, 127f)
    val typed = bundle.get(Keys.float)

    assertThat(typed).isEqualTo(127f)
  }

  @Test fun testNullFloat() {
    assertThat(bundle.get(Keys.nullFloat)).isNull()

    bundle.set(Keys.nullFloat, 52f)
    val raw = bundle.getString("nullFloat", null)
    val typed = bundle.get(Keys.nullFloat)

    assertThat(raw).isEqualTo("52.0")
    assertThat(typed).isEqualTo(52f)
  }

  @Test fun testInt() {
    assertThat(bundle.get(Keys.int)).isEqualTo(42)

    bundle.set(Keys.int, 127)
    val raw = bundle.getInt("int", -1)
    val typed = bundle.get(Keys.int)

    assertThat(raw).isEqualTo(127)
    assertThat(typed).isEqualTo(127)
  }

  @Test fun testNullInt() {
    assertThat(bundle.get(Keys.nullInt)).isNull()

    bundle.set(Keys.nullInt, 52)
    val raw = bundle.getString("nullInt", null)
    val typed = bundle.get(Keys.nullInt)

    assertThat(raw).isEqualTo("52")
    assertThat(typed).isEqualTo(52)
  }

  @Test fun testLong() {
    assertThat(bundle.get(Keys.long)).isEqualTo(42L)

    bundle.set(Keys.long, 127L)
    val raw = bundle.getLong("long", -1)
    val typed = bundle.get(Keys.long)

    assertThat(raw).isEqualTo(127)
    assertThat(typed).isEqualTo(127)
  }

  @Test fun testNullLong() {
    assertThat(bundle.get(Keys.nullLong)).isNull()

    bundle.set(Keys.nullLong, 52)
    val raw = bundle.getString("nullLong", null)
    val typed = bundle.get(Keys.nullLong)

    assertThat(raw).isEqualTo("52")
    assertThat(typed).isEqualTo(52)
  }

  @Test fun testString() {
    assertThat(bundle.get(Keys.string)).isEqualTo("default")

    bundle.set(Keys.string, "hi")
    val raw = bundle.getString("string", null)
    val typed = bundle.get(Keys.string)

    assertThat(raw).isEqualTo("hi")
    assertThat(typed).isEqualTo("hi")
  }

  @Test fun testNullString() {
    assertThat(bundle.get(Keys.nullString)).isNull()

    bundle.set(Keys.nullString, "yo")
    val raw = bundle.getString("nullString", null)
    val typed = bundle.get(Keys.nullString)

    assertThat(raw).isEqualTo("yo")
    assertThat(typed).isEqualTo("yo")
  }

  @Test fun testPersistableBundle() {
    val newBundle = PersistableBundle()
    bundle.putString("someString", "makes this non-empty")

    assertThat(bundle.get(Keys.persistableBundle)).isNull()
    bundle.set(Keys.persistableBundle, newBundle)
    val raw = bundle.getPersistableBundle("persistableBundle")
    val typed = bundle.get(Keys.persistableBundle)

    assertThat(raw).isEqualTo(newBundle)
    assertThat(typed).isEqualTo(newBundle)
  }
}
