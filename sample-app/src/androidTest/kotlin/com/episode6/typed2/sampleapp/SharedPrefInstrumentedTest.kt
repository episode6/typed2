package com.episode6.typed2.sampleapp

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.test.platform.app.InstrumentationRegistry
import assertk.assertThat
import assertk.assertions.*
import com.episode6.typed2.*
import com.episode6.typed2.sharedprefs.PrefKeyNamespace
import com.episode6.typed2.sharedprefs.get
import com.episode6.typed2.sharedprefs.set
import org.junit.After
import org.junit.Before
import org.junit.Test

class SharedPrefInstrumentedTest {
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

  private val sharedPrefs: SharedPreferences by lazy {
    InstrumentationRegistry.getInstrumentation().targetContext
      .getSharedPreferences("SharedPrefInstrumentedTest", Context.MODE_PRIVATE)
  }


  @Before fun setup() {
    sharedPrefs.edit(true) { clear() }
  }

  @After fun tearDown() {
    sharedPrefs.edit(true) { clear() }
  }

  @Test fun testBool() {
    assertThat(sharedPrefs.get(Keys.bool)).isTrue()

    sharedPrefs.edit(true) { set(Keys.bool, false) }
    val raw = sharedPrefs.getBoolean("bool", true)
    val typed = sharedPrefs.get(Keys.bool)

    assertThat(raw).isFalse()
    assertThat(typed).isFalse()
  }

  @Test fun testNullBool() {
    assertThat(sharedPrefs.get(Keys.nullBool)).isNull()

    sharedPrefs.edit(true) { set(Keys.nullBool, true) }
    val raw = sharedPrefs.getString("nullBool", null)
    val typed = sharedPrefs.get(Keys.nullBool)

    assertThat(raw).isEqualTo("true")
    assertThat(typed).isNotNull().isTrue()
  }

  @Test fun testFloat() {
    assertThat(sharedPrefs.get(Keys.float)).isEqualTo(12.5f)

    sharedPrefs.edit(true) { set(Keys.float, 127f) }
    val raw = sharedPrefs.getFloat("float", -1f)
    val typed = sharedPrefs.get(Keys.float)

    assertThat(raw).isEqualTo(127f)
    assertThat(typed).isEqualTo(127f)
  }

  @Test fun testNullFloat() {
    assertThat(sharedPrefs.get(Keys.nullFloat)).isNull()

    sharedPrefs.edit(true) { set(Keys.nullFloat, 52f) }
    val raw = sharedPrefs.getString("nullFloat", null)
    val typed = sharedPrefs.get(Keys.nullFloat)

    assertThat(raw).isEqualTo("52.0")
    assertThat(typed).isEqualTo(52f)
  }

  @Test fun testInt() {
    assertThat(sharedPrefs.get(Keys.int)).isEqualTo(42)

    sharedPrefs.edit(true) { set(Keys.int, 127) }
    val raw = sharedPrefs.getInt("int", -1)
    val typed = sharedPrefs.get(Keys.int)

    assertThat(raw).isEqualTo(127)
    assertThat(typed).isEqualTo(127)
  }

  @Test fun testNullInt() {
    assertThat(sharedPrefs.get(Keys.nullInt)).isNull()

    sharedPrefs.edit(true) { set(Keys.nullInt, 52) }
    val raw = sharedPrefs.getString("nullInt", null)
    val typed = sharedPrefs.get(Keys.nullInt)

    assertThat(raw).isEqualTo("52")
    assertThat(typed).isEqualTo(52)
  }

  @Test fun testLong() {
    assertThat(sharedPrefs.get(Keys.long)).isEqualTo(42L)

    sharedPrefs.edit(true) { set(Keys.long, 127L) }
    val raw = sharedPrefs.getLong("long", -1)
    val typed = sharedPrefs.get(Keys.long)

    assertThat(raw).isEqualTo(127)
    assertThat(typed).isEqualTo(127)
  }

  @Test fun testNullLong() {
    assertThat(sharedPrefs.get(Keys.nullLong)).isNull()

    sharedPrefs.edit(true) { set(Keys.nullLong, 52) }
    val raw = sharedPrefs.getString("nullLong", null)
    val typed = sharedPrefs.get(Keys.nullLong)

    assertThat(raw).isEqualTo("52")
    assertThat(typed).isEqualTo(52)
  }

  @Test fun testString() {
    assertThat(sharedPrefs.get(Keys.string)).isEqualTo("default")

    sharedPrefs.edit(true) { set(Keys.string, "hi") }
    val raw = sharedPrefs.getString("string", null)
    val typed = sharedPrefs.get(Keys.string)

    assertThat(raw).isEqualTo("hi")
    assertThat(typed).isEqualTo("hi")
  }

  @Test fun testNullString() {
    assertThat(sharedPrefs.get(Keys.nullString)).isNull()

    sharedPrefs.edit(true) { set(Keys.nullString, "yo") }
    val raw = sharedPrefs.getString("nullString", null)
    val typed = sharedPrefs.get(Keys.nullString)

    assertThat(raw).isEqualTo("yo")
    assertThat(typed).isEqualTo("yo")
  }
}
