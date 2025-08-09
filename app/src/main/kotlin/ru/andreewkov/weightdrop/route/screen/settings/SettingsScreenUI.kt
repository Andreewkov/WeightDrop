package ru.andreewkov.weightdrop.route.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.widget.ValuePanelWidget

private data class SettingItem(
    val title: String,
    val text: String,
    val onClick: () -> Unit = { },
)

@Composable
fun SettingsScreenUI() {
    Content(
        items = listOf(
            SettingItem("Целевой вес", "80.0"),
            SettingItem("Целевой вес", "80.0"),
            SettingItem("Целевой вес", "80.0"),
        )
    )
}

@Composable
private fun Content(
    items: List<SettingItem>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().wrapContentHeight()
    ) {
        items.forEach { item ->
            ValuePanelWidget(
                title = item.title,
                text = item.text,
                tintColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
            )
        }
    }
}

@WeightDropPreview
@Composable
private fun ContentPreview() {
    WeightDropTheme {
        Scaffold { innerPadding ->
            Content(
                items = listOf(
                    SettingItem("Целевой вес", "80.0"),
                    SettingItem("Целевой вес", "80.0"),
                    SettingItem("Целевой вес", "80.0"),
                ),
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
