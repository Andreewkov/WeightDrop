package ru.andreewkov.weightdrop.route.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.model.SettingItem
import ru.andreewkov.weightdrop.model.SettingsDialogValue
import ru.andreewkov.weightdrop.model.SettingsItemType
import ru.andreewkov.weightdrop.route.dialog.picker.DatePickerDialogUI
import ru.andreewkov.weightdrop.route.dialog.picker.HeightPickerDialogUI
import ru.andreewkov.weightdrop.route.dialog.picker.WeightPickerDialogUI
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.ScaffoldPreview
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.widget.ValuePanelWidget

@Composable
fun SettingsScreenUI() {
    val viewModel: SettingsScreenViewModel = hiltViewModel()
    val screenState by viewModel.screenState.collectAsState()

    when (val state = screenState.content) {
        SettingsContentState.Loading -> Unit
        is SettingsContentState.Success -> {
            Content(
                items = state.items,
                onItemClick = viewModel::onSettingsTypeClick,
            )
        }
        SettingsContentState.Failure -> Unit
    }
    screenState.displayedDialog?.let { dialogValue ->
        Dialog(
            dialogValue = dialogValue,
            onValueChanged = viewModel::onDialogValueChanged,
            onDismissRequest = viewModel::onDialogDismissRequest,
        )
    }
}

@Composable
private fun Content(
    items: List<SettingItem>,
    onItemClick: (SettingsItemType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .padding(20.dp)
            .fillMaxSize(),
    ) {
        items.forEach { item ->
            ValuePanelWidget(
                title = stringResource(item.titleRes),
                text = if (item.text != null && item.textPostfixRes != null) {
                    "${item.text} ${stringResource(item.textPostfixRes)}"
                } else {
                    item.text ?: "-"
                },
                tintColor = Color.White,
                iconPainter = painterResource(item.iconRes),
                onClick = {
                    onItemClick(item.type)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
            )
        }
    }
}

@Composable
private fun Dialog(
    dialogValue: SettingsDialogValue<*>,
    onValueChanged: (SettingsDialogValue<*>) -> Unit,
    onDismissRequest: () -> Unit,
) {
    when (dialogValue) {
        is SettingsDialogValue.HeightValue -> {
            HeightPickerDialogUI(
                initialHeight = dialogValue.value,
                titleRes = R.string.settings_height_title,
                onHeightPicked = { height ->
                    onValueChanged(
                        SettingsDialogValue.HeightValue(height),
                    )
                },
                onDismissRequest = onDismissRequest,
            )
        }
        is SettingsDialogValue.StartWeightValue -> {
            WeightPickerDialogUI(
                initialWeight = dialogValue.value,
                titleRes = R.string.settings_start_weight_title,
                onWeightPicked = { weight ->
                    onValueChanged(
                        SettingsDialogValue.StartWeightValue(weight),
                    )
                },
                onDismissRequest = onDismissRequest,
            )
        }
        is SettingsDialogValue.TargetWeightValue -> {
            WeightPickerDialogUI(
                initialWeight = dialogValue.value,
                titleRes = R.string.settings_target_weight_title,
                onWeightPicked = { weight ->
                    onValueChanged(
                        SettingsDialogValue.TargetWeightValue(weight),
                    )
                },
                onDismissRequest = onDismissRequest,
            )
        }
        is SettingsDialogValue.StartDateValue -> {
            DatePickerDialogUI(
                initialDate = dialogValue.value,
                titleRes = R.string.settings_start_date_title,
                onDatePicked = { date ->
                    onValueChanged(
                        SettingsDialogValue.StartDateValue(date),
                    )
                },
                onDismissRequest = onDismissRequest,
            )
        }
    }
}

@WeightDropPreview
@Composable
private fun ContentPreview() {
    WeightDropTheme {
        ScaffoldPreview {
            Content(
                items = listOf(
                    SettingItem(
                        SettingsItemType.Height,
                        R.string.settings_height_title,
                        "187",
                        R.drawable.ic_man,
                    ),
                    SettingItem(
                        SettingsItemType.StartWeight,
                        R.string.settings_start_weight_title,
                        "112.0",
                        R.drawable.ic_floor_scales,
                    ),
                    SettingItem(
                        SettingsItemType.TargetWeight,
                        R.string.settings_height_title,
                        "80.0",
                        R.drawable.ic_weight_scales,
                    ),
                    SettingItem(
                        SettingsItemType.StartDate,
                        R.string.settings_start_date_title,
                        "27.09.2025",
                        R.drawable.ic_calendar,
                    ),
                ),
                onItemClick = { },
            )
        }
    }
}
