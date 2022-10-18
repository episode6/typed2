package com.episode6.typed2

import android.content.SharedPreferences
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.episode6.typed2.sharedprefs.PrefKeyNamespace
import com.episode6.typed2.sharedprefs.get
import com.episode6.typed2.sharedprefs.set
import com.episode6.typed2.sharedprefs.stringSet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.*

class SharedPrefTest {

  object Keys : PrefKeyNamespace("com.prefix.") {
    val myInt = key("intKey").int(default = 42)
    val myNullInt = key("nullableInt").int()
    val myStringSet = key("stringSet").stringSet()
    val myAsyncString = key("asyncString").string().async()
  }

  val editor: SharedPreferences.Editor = mock()
  val sharedPrefs: SharedPreferences = mock {
    on { edit() } doReturn editor
  }


  @Test
  fun testGetInt() {
    sharedPrefs.stub {
      on { getInt("com.prefix.intKey", 42) } doReturn 24
    }

    val result: Int = sharedPrefs.get(Keys.myInt)

    verify(sharedPrefs).getInt("com.prefix.intKey", 42)
    assertThat(result).isEqualTo(24)
  }

  @Test fun testSetInt() {
    sharedPrefs.edit().set(Keys.myInt, 36)

    verify(editor).putInt("com.prefix.intKey", 36)
  }

  @Test
  fun testGetNullableInt() {
    sharedPrefs.stub {
      on { contains("com.prefix.nullableInt") } doReturn true
      on { getString(eq("com.prefix.nullableInt"), anyOrNull()) } doReturn "112"
    }

    val result: Int? = sharedPrefs.get(Keys.myNullInt)

    verify(sharedPrefs).getString(eq("com.prefix.nullableInt"), anyOrNull())
    assertThat(result).isEqualTo(112)
  }

  @Test
  fun testGetNullInt() {
    sharedPrefs.stub {
      on { getString(eq("com.prefix.nullableInt"), anyOrNull()) } doReturn null
    }

    val result: Int? = sharedPrefs.get(Keys.myNullInt)

    assertThat(result).isNull()
  }

  @Test fun testSetNullableInt() {
    sharedPrefs.edit().set(Keys.myNullInt, 36)

    verify(editor).putString("com.prefix.nullableInt", "36")
  }

  @Test fun testStringSet() {
    sharedPrefs.stub {
      on { contains("com.prefix.stringSet") } doReturn true
      on { getStringSet(eq("com.prefix.stringSet"), anyOrNull()) } doReturn setOf("hi")
    }

    val result = sharedPrefs.get(Keys.myStringSet)

    assertThat(result).isEqualTo(setOf("hi"))
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test fun testAsyncString() = runTest {
    sharedPrefs.stub {
      on { getString("com.prefix.asyncString", null) } doReturn "happyString"
    }

    val result = sharedPrefs.get(Keys.myAsyncString)

    assertThat(result).isEqualTo("happyString")
  }
}
