@file:OptIn(ExperimentalCoroutinesApi::class)

package com.episode6.typed2.sampleapp

import android.content.Intent
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.episode6.typed2.bundles.BundleKeyNamespace
import com.episode6.typed2.bundles.getExtra
import com.episode6.typed2.bundles.setExtra
import com.episode6.typed2.int
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

class IntentInstrumentedTest {
  private object Keys : BundleKeyNamespace() {
    val int = key("int").int()
    val intAsync = key("intAsync").int().async()
  }

  private val intent = Intent()

  @Test fun testInt() {
    assertThat(intent.getExtra(Keys.int)).isNull()

    intent.setExtra(Keys.int, 101)
    val raw = intent.getStringExtra("int")
    val typed = intent.getExtra(Keys.int)

    assertAll {
      assertThat(raw).isEqualTo("101")
      assertThat(typed).isEqualTo(101)
    }
  }

  @Test fun testAsyncInt() = runTest {
    assertThat(intent.getExtra(Keys.intAsync)).isNull()

    intent.setExtra(Keys.intAsync, 101)
    val raw = intent.getStringExtra("intAsync")
    val typed = intent.getExtra(Keys.intAsync)

    assertAll {
      assertThat(raw).isEqualTo("101")
      assertThat(typed).isEqualTo(101)
    }
  }
}
