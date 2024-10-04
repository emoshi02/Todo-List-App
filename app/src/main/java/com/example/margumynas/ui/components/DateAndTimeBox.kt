package com.example.margumynas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SelectedDateAndTimeBox(text: String, value: String, modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .height(50.dp)
            .background(MaterialTheme.colorScheme.tertiary)
            .fillMaxWidth()
            .padding(start = 16.dp),
    ) {
        Text(
            text = "$text $value",
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}