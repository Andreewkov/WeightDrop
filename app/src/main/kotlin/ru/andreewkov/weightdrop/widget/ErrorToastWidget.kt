package ru.andreewkov.weightdrop.widget

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.theme.Grey
import ru.andreewkov.weightdrop.theme.Red
import ru.andreewkov.weightdrop.theme.WeightDropTheme

@Composable
fun ErrorToastWidget(
    containerColor: Color,
    contentColor: Color,
    @StringRes text: Int,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(containerColor)
            .padding(20.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_error),
            contentDescription = null,
            tint = contentColor,
        )

        Text(
            text = stringResource(text),
            style = TextStyle(
                color = contentColor,
            ),
        )
    }
}

@Preview
@Composable
private fun ErrorTostWidgetPreview() {
    WeightDropTheme {
        ErrorToastWidget(
            containerColor = Red,
            contentColor = Grey,
            text = R.string.error_toast_info,
        )
    }
}
