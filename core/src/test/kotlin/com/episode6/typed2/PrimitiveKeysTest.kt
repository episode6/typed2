package com.episode6.typed2

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.episode6.typed2.sharedprefs.PrefKeyNamespace
import org.junit.Test
import org.mockito.kotlin.*

class PrimitiveKeysTest {

  object Keys : PrefKeyNamespace() {
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

  private val getter: PrimitiveKeyValueGetter = mock {
    on { contains(any()) } doReturn false
    on { getBoolean(any(), any()) } doAnswer { it.getArgument(1) }
    on { getFloat(any(), any()) } doAnswer { it.getArgument(1) }
    on { getInt(any(), any()) } doAnswer { it.getArgument(1) }
    on { getLong(any(), any()) } doAnswer { it.getArgument(1) }
    on { getString(any(), anyOrNull()) } doAnswer { it.getArgument(1) }
  }

  private val setter: PrimitiveKeyValueSetter = mock()

  private fun <T> PrimitiveKeyValueGetter.get(key: PrimitiveKey<T, *>): T = key.get(this)
  private fun <T> PrimitiveKeyValueSetter.set(key: PrimitiveKey<T, *>, value: T) = key.set(this, value)

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
