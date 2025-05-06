package ru.andreewkov.weightdrop.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.ui.theme.Dark
import ru.andreewkov.weightdrop.ui.theme.DarkLight
import ru.andreewkov.weightdrop.ui.theme.Grey
import ru.andreewkov.weightdrop.ui.theme.GreyLight
import ru.andreewkov.weightdrop.ui.theme.Peach
import ru.andreewkov.weightdrop.ui.theme.PeachLight

data class DatePanelWidgetColors(
    val containerColor: Color,
    val backgroundColor: Color,
    val dateColor: Color,
)

@Composable
fun DatePanelWidget(
    colors: DatePanelWidgetColors,
    date: String,
    modifier: Modifier = Modifier,
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val heightDp by with(LocalDensity.current) {
        remember {
            derivedStateOf { size.height.toDp() }
        }
    }
    Row(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { size = it }
            .clip(RoundedCornerShape(heightDp / 4))
            .background(colors.containerColor)
            .padding(heightDp / 10)
    ) {
        CalendarBox(
            cornerRadius = heightDp / 4,
            backgroundColor = colors.backgroundColor,
            iconColor = colors.dateColor,
        )
        Spacer(modifier = Modifier.size(heightDp / 10))
        DateBox(
            cornerRadius = heightDp / 4,
            text = date,
            textSize = with(LocalDensity.current) { heightDp.toSp() * 0.5f },
            backgroundColor = colors.backgroundColor,
            dateColor = colors.dateColor,
        )
    }
}

@Composable
private fun CalendarBox(
    cornerRadius: Dp,
    backgroundColor: Color,
    iconColor: Color,
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .shadow(
                elevation = cornerRadius,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = iconColor,
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_calendar),
            contentDescription = "",
            tint = iconColor,
            modifier = Modifier
                .padding(cornerRadius * 2 / 3)
                .aspectRatio(1f)
                .fillMaxSize()
        )
    }
}

context(RowScope)
@Composable
private fun DateBox(
    cornerRadius: Dp,
    text: String,
    textSize: TextUnit,
    backgroundColor: Color,
    dateColor: Color,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxSize()
            .shadow(
                elevation = cornerRadius,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = dateColor,
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
    ) {
        Text(
            text = text,
            style = TextStyle(
                textAlign = TextAlign.Start,
                fontSize = textSize,
                color = dateColor,
                baselineShift = BaselineShift.None,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = cornerRadius * 2 / 3)
                .align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun DatePanelWidgetPreview() {
    MaterialTheme {
        DatePanelWidget(
            colors = DatePanelWidgetColors(
                containerColor = Peach,
                backgroundColor = PeachLight,
                dateColor = Dark,
            ),
            date = "12.06.2007",
            modifier = Modifier
                .height(40.dp)
                .width(200.dp)
                .padding(5.dp)
        )
    }
}

@Preview
@Composable
private fun DatePanelWidgetPreviewInverse() {
    MaterialTheme {
        DatePanelWidget(
            colors = DatePanelWidgetColors(
                containerColor = Dark,
                backgroundColor = DarkLight,
                dateColor = Grey,
            ),
            date = "07.10.1977",
            modifier = Modifier
                .height(100.dp)
                .width(400.dp)
                .padding(5.dp)
        )
    }
}
