package com.episode6.typed2

import android.content.SharedPreferences
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.episode6.typed2.plugins.get
import com.episode6.typed2.plugins.set
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

object Keys : KeyNamespace("com.prefix.") {

  val myInt = key("intKey").int(defaultValue = 42)
  val myNullInt = key("nullableInt").nullableInt()
}

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

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
      on { getString("com.prefix.nullableInt", null) } doReturn "112"
    }

    val result: Int? = sharedPrefs.get(Keys.myNullInt)

    verify(sharedPrefs).getString("com.prefix.nullableInt", null)
    assertThat(result).isEqualTo(112)
  }

  @Test
  fun testGetNullInt() {
    sharedPrefs.stub {
      on { contains("com.prefix.nullableInt") } doReturn false
      on { getString("com.prefix.nullableInt", null) } doReturn null
    }

    val result: Int? = sharedPrefs.get(Keys.myNullInt)

    verify(sharedPrefs).contains("com.prefix.nullableInt")
    assertThat(result).isNull()
  }

  @Test fun testSetNullableInt() {
    sharedPrefs.edit().set(Keys.myNullInt, 36)

    verify(editor).putString("com.prefix.nullableInt", "36")
  }
}
