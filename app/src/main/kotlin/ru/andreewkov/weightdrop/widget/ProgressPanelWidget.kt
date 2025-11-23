package ru.andreewkov.weightdrop.widget

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.WeightingFormatter
import ru.andreewkov.weightdrop.theme.Peach
import ru.andreewkov.weightdrop.theme.PeachLight
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.utils.roundToDecimals

data class ProgressPanelWidgetState(
    val startWeight: Float?,
    val currentWeight: Float?,
    val targetWeight: Float?,
) {

    fun getPercentProgress(): Float? {
        return if (startWeight != null && currentWeight != null && targetWeight != null) {
            val firstStep = startWeight - currentWeight
            val all = startWeight - targetWeight
            (firstStep / all * 100).roundToDecimals(100)
        } else {
            null
        }
    }

    fun getDifferenceProgress(): String {
        if (startWeight == null || currentWeight == null) return "-"
        val progress = currentWeight - startWeight
        val result = WeightingFormatter.formatWeightLong(progress)

        return if (progress > 0) {
            "+$result"
        } else {
            result
        }
    }
}

private interface WeightScope {
    fun Modifier.setWeight(weight: Float): Modifier
}

@Composable
fun ProgressPanelWidget(
    primaryColor: Color,
    secondaryColor: Color,
    state: ProgressPanelWidgetState,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val isLandscape by remember {
        mutableStateOf(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
    }
    val progress by remember {
        derivedStateOf {
            val start = state.startWeight
            val target = state.targetWeight
            val current = state.currentWeight

            if (start != null && target != null && current != null) {
                "${((start - current) / (start - target) * 100).roundToDecimals(100)}%"
            } else {
                null
            }
        }
    }

    val progressValue by remember {
        derivedStateOf {
            val current = state.currentWeight
            val start = state.startWeight
            if (current != null && start != null) {
                val result = current - start

                if (result > 0) {
                    "+${WeightingFormatter.formatWeightLong(result)}"
                } else {
                    WeightingFormatter.formatWeightLong(result)
                }
            } else {
                "-"
            }
        }
    }

    Container(
        isVertical = isLandscape,
        modifier = modifier,
    ) {
        Container(
            isVertical = !isLandscape,
            modifier = Modifier
                .fillMaxSize()
                .setWeight(getTextWeight(isLandscape)),
        ) {
            TextItem(
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                titleRes = R.string.progress_panel_start_weight,
                value = WeightingFormatter.formatWeightLong(state.startWeight),
            )
            TextItem(
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                titleRes = R.string.progress_panel_target_weight,
                value = WeightingFormatter.formatWeightLong(state.targetWeight),
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        CircleProgressWidget(
            trackColor = primaryColor,
            valueColor = secondaryColor,
            progress = (state.getPercentProgress() ?: 0f) / 100,
            progressValue = state.getDifferenceProgress(),
            modifier = Modifier.setWeight(1f),
        )

        Spacer(modifier = Modifier.size(16.dp))

        Container(
            isVertical = !isLandscape,
            modifier = Modifier
                .fillMaxSize()
                .setWeight(getTextWeight(isLandscape)),
        ) {
            TextItem(
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                titleRes = R.string.progress_panel_current_weight,
                value = WeightingFormatter.formatWeightLong(state.currentWeight),
            )
            TextItem(
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                titleRes = R.string.progress_panel_percent_success,
                value = state.getPercentProgress()?.toString(),
            )
        }
    }
}

@Composable
private fun TextItem(
    primaryColor: Color,
    secondaryColor: Color,
    @StringRes titleRes: Int,
    value: String?,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(titleRes),
            style = TextStyle(
                color = primaryColor,
                fontWeight = FontWeight.W600,
            ),
        )
        Text(
            text = value ?: "-",
            style = TextStyle(
                color = secondaryColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.W600,
            ),
        )
    }
}

@Composable
private fun Container(
    isVertical: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable WeightScope.() -> Unit,
) {
    if (isVertical) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier,
            content = {
                val setter = object : WeightScope {
                    override fun Modifier.setWeight(weight: Float) = weight(weight)
                }
                content(setter)
            },
        )
    } else {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
            content = {
                val setter = object : WeightScope {
                    override fun Modifier.setWeight(weight: Float) = weight(weight)
                }
                content(setter)
            },
        )
    }
}

private fun getTextWeight(isLandscape: Boolean): Float {
    return if (isLandscape) 0.5f else 1f
}

@Composable
@Preview(device = "spec:width=400dp,height=800dp,isRound=false,orientation=portrait")
private fun WidgetPreviewHorizontal() {
    WeightDropTheme {
        ProgressPanelWidget(
            primaryColor = Peach,
            secondaryColor = PeachLight,
            state = ProgressPanelWidgetState(
                startWeight = 110f,
                currentWeight = 101.1f,
                targetWeight = 80f,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
        )
    }
}

@Composable
@Preview(device = "spec:width=400dp,height=800dp,isRound=false,orientation=landscape")
private fun WidgetPreviewVertical() {
    WeightDropTheme {
        ProgressPanelWidget(
            primaryColor = Peach,
            secondaryColor = PeachLight,
            state = ProgressPanelWidgetState(
                startWeight = 110f,
                currentWeight = 102.0f,
                targetWeight = 80f,
            ),
            modifier = Modifier
                .fillMaxHeight()
                .width(260.dp),
        )
    }
}
