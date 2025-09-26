package ru.andreewkov.weightdrop.widget

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun DialogCard(
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            colors = cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            content = content,
        )
    }
}
