package ru.andreewkov.weightdrop.widget

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.util.dpToPx
import ru.andreewkov.weightdrop.util.pxToDp

@Composable
fun ValuePanelWidget(
    title: String,
    text: String,
    tintColor: Color,
    modifier: Modifier = Modifier,
    iconPainter: Painter? = null,
    onClick: () -> Unit = { },
) {
    Row(
        modifier = modifier.wrapContentSize(),
    ) {
        iconPainter?.let { painter ->
            Icon(
                painter = iconPainter,
                tint = tintColor,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically),
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        Panel(
            title = title,
            text = text,
            tintColor = tintColor,
            modifier = Modifier.weight(1f),
            onClick = onClick,
        )
    }
}

@Composable
fun Panel(
    title: String,
    text: String,
    tintColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult: TextLayoutResult = textMeasurer.measure(
        text = AnnotatedString(title),
        style =
            TextStyle(
                color = tintColor,
                textAlign = TextAlign.Center,
            ),
    )
    var textSize by remember { mutableStateOf(IntSize.Zero) }
    val textBoxRectSize by with(LocalDensity.current) {
        remember {
            derivedStateOf {
                Size(textSize.width.toFloat() + 16.dp.toPx(), 4.dp.toPx())
            }
        }
    }
    val textLeftOffset = 8.dp.dpToPx()
    Box(modifier = modifier) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = (textLayoutResult.size.height / 2f).pxToDp() - 1.dp)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        color = tintColor,
                        size = textBoxRectSize,
                        topLeft = Offset(x = textLeftOffset, y = 0f),
                        blendMode = BlendMode.Clear,
                    )
                }
                .padding(1.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onClick)
                .border(
                    width = 2.dp,
                    color = tintColor,
                    shape = RoundedCornerShape(8.dp),
                ),
        ) {
            Text(
                text = text,
                style = TextStyle(
                    color = tintColor,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                ),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.Center),
            )
        }
        Text(
            text = title,
            style = TextStyle(
                color = tintColor,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
            ),
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .onSizeChanged { textSize = it },
        )
    }
}

@Preview
@Composable
private fun ValuePanelWidgetPreview() {
    MaterialTheme {
        ValuePanelWidget(
            title = "Дата",
            text = "06.06.2025",
            tintColor = Color.White,
            modifier = Modifier
                .padding(5.dp)
                .width(240.dp)
                .height(48.dp),
            iconPainter = painterResource(R.drawable.ic_calendar),
        )
    }
}
