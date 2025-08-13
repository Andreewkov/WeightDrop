package ru.andreewkov.weightdrop.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andreewkov.weightdrop.theme.WeightDropTheme

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onClick: () -> Unit = { },
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
        ),
        enabled = isEnabled,
        modifier = modifier
            .height(42.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSecondary,
            ),
        )
    }
}

@Composable
@Preview
private fun AppButtonEnabledPreview() {
    WeightDropTheme {
        AppButton(text = "Выбрать")
    }
}

@Composable
@Preview
private fun AppButtonDisabledPreview() {
    WeightDropTheme {
        AppButton(text = "Выбрать", isEnabled = false)
    }
}
