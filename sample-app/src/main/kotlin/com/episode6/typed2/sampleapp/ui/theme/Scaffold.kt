@file:OptIn(ExperimentalMaterial3Api::class)

package com.episode6.typed2.sampleapp.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable fun AppScaffold(
  title: String,
  navigationIcon: @Composable () -> Unit = {},
  actions: @Composable RowScope.() -> Unit = {},
  content: @Composable () -> Unit,
) {
  AppScaffold(
    title = { Text(text = title) },
    navigationIcon = navigationIcon,
    actions = actions,
    content = content,
  )
}

@Composable fun AppScaffold(
  title: @Composable () -> Unit,
  navigationIcon: @Composable () -> Unit = {},
  actions: @Composable RowScope.() -> Unit = {},
  content: @Composable () -> Unit,
) {
  AppTheme {
    Scaffold(
      topBar = {
        SmallTopAppBar(
          title = title,
          navigationIcon = navigationIcon,
          actions = actions,
        )
      },
      content = { padding ->
        Box(modifier = Modifier.padding(padding)) {
          content()
        }
      },
    )
  }
}
