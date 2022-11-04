package com.episode6.typed2.sampleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.episode6.typed2.sampleapp.nav.AppNavGraph
import com.episode6.typed2.sampleapp.nav.ScreenRegistration
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint class MainActivity : ComponentActivity() {

  private val viewModel: MainActivityViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AppNavGraph(viewModel.screens)
    }
  }
}

@HiltViewModel class MainActivityViewModel @Inject constructor(
  val screens: Set<@JvmSuppressWildcards ScreenRegistration>,
) : ViewModel()
