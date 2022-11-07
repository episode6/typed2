package com.episode6.typed2.sampleapp.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable fun FullWidthButton(text: String, onClick: () -> Unit) {
  Button(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
    Box(contentAlignment = Alignment.Center) {
      Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun TextCard(
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
) = OutlinedTextField(
  value = value,
  onValueChange = onValueChange,
  label = { Text(text = label) },
  keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Ascii)
)
