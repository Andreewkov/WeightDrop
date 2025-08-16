package ru.andreewkov.weightdrop.widget

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.theme.WeightDropTheme

@Composable
fun DialogContainer(
    @StringRes buttonTextRes: Int,
    onButtonClick: () -> Unit,
    @StringRes titleRes: Int? = null,
    isButtonEnabled: () -> Boolean = { true },
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(
                horizontal = 32.dp,
                vertical = 16.dp,
            ),
    ) {
        titleRes?.let {
            Text(
                text = stringResource(titleRes),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        content()

        Spacer(modifier = Modifier.size(16.dp))

        val isEnabled by remember { derivedStateOf { isButtonEnabled() } }
        AppButton(
            text = stringResource(buttonTextRes),
            isEnabled = isEnabled,
            onClick = onButtonClick,
        )
    }
}

@Preview
@Composable
private fun DialogContainerPreview() {
    WeightDropTheme {
        Card(
            colors = cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            DialogContainer(
                titleRes = R.string.dialog_date_picker_title,
                buttonTextRes = R.string.dialog_date_picker_button,
                onButtonClick = { },
            ) {
                Spacer(modifier = Modifier.size(200.dp))
            }
        }
    }
}
