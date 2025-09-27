package ru.andreewkov.weightdrop.route.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.model.SettingItem
import ru.andreewkov.weightdrop.model.SettingsItemType
import ru.andreewkov.weightdrop.model.SettingsUpdateValue
import ru.andreewkov.weightdrop.theme.WeightDropTheme
import ru.andreewkov.weightdrop.util.WeightDropPreview
import ru.andreewkov.weightdrop.widget.ValuePanelWidget

@Composable
fun SettingsScreenUI(
    valueUpdatedFlow: SharedFlow<SettingsUpdateValue<*>>,
    onSettingItemClick: (SettingsItemType) -> Unit,
) {
    val viewModel: SettingsScreenViewModel = hiltViewModel()
    val screenState by viewModel.screenState.collectAsState()

    LaunchedEffect(Unit) {
        valueUpdatedFlow.onEach { settingsUpdateValue ->
            viewModel.updateSettingsValue(settingsUpdateValue)
        }.launchIn(this)
    }

    when (val state = screenState) {
        SettingsScreenState.Loading -> Unit
        is SettingsScreenState.Success -> {
            Content(
                items = state.items,
                onItemClick = onSettingItemClick,
            )
        }
        SettingsScreenState.Failure -> Unit
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
                text = item.text,
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

@WeightDropPreview
@Composable
private fun ContentPreview() {
    WeightDropTheme {
        Scaffold { innerPadding ->
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
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
