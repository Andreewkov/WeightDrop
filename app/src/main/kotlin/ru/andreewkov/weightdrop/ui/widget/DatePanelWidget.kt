package ru.andreewkov.weightdrop.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DatePanelWidget() {

}

@Preview
@Composable
private fun DatePanelWidgetPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .height(50.dp)
                .width(200.dp)
        ) {
            DatePanelWidget()
        }
    }
}
