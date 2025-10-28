package ru.andreewkov.weightdrop.route.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.ScaffoldPreview
import ru.andreewkov.weightdrop.widget.LoadingWidget

@Composable
fun LoadingScreenUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LoadingWidget(
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .size(76.dp)
                .align(Alignment.Center),
        )
    }
}

@Composable
@Preview
private fun Preview() {
    WeightDropTheme {
        ScaffoldPreview {
            LoadingScreenUI()
        }
    }
}
