package com.episode6.typed2.sampleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.episode6.typed2.sampleapp.ui.theme.AppScaffold

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AppScaffold(title = "Title") {
        Text(text = "Hello")
      }
    }
  }
}
