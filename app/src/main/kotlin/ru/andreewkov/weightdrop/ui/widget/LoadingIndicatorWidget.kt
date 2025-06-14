package ru.andreewkov.weightdrop.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme

@Composable
fun LoadingIndicatorWidget(
    color: Color,
    trackColor: Color,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator(
            color = color,
            strokeWidth = size / 15,
            trackColor = trackColor,
            modifier = Modifier
                .size(size)
                .align(Alignment.Center),
        )
    }
}

@Composable
@Preview
fun LoadingIndicatorWidgetPreview() {
    WeightDropTheme {
        LoadingIndicatorWidget(
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(200.dp),
        )
    }
}
