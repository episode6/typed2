package com.episode6.typed2.sampleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.episode6.typed2.sampleapp.ui.theme.AppScaffold
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint class MainActivity : ComponentActivity() {

  private val viewModel: MainActivityViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AppScaffold {
        Text(text = viewModel.sampleText, modifier = Modifier.padding(8.dp).testTag("TEST_TAG"))
      }
    }
  }
}

@HiltViewModel class MainActivityViewModel @Inject constructor() : ViewModel() {
  val sampleText = "Hello there"
}
