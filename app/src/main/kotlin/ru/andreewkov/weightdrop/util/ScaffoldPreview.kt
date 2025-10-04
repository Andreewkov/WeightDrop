package ru.andreewkov.weightdrop.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScaffoldPreview(content: @Composable BoxScope.() -> Unit) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
            content = content,
        )
    }
}
