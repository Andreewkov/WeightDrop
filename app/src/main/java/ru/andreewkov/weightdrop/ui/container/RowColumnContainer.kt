package ru.andreewkov.weightdrop.ui.container

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration

typealias AddWeight = Modifier.(weight: Float) -> Modifier

@Composable
fun RowColumnContainer(
    isRow: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (addWeight: AddWeight) -> Unit,
) {
    if (isRow) {
        Row {
            content({ then(Modifier.weight(it)) })
        }
    } else {
        Column {
            content({ then(Modifier.weight(it)) })
        }
    }
}