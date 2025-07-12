package ru.andreewkov.weightdrop.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.ui.screen.AppAction
import ru.andreewkov.weightdrop.ui.screen.AppActionHandler
import ru.andreewkov.weightdrop.ui.screen.Route
import ru.andreewkov.weightdrop.ui.screen.appActionHandlerStub
import ru.andreewkov.weightdrop.ui.util.isPortrait

data class NavigationBarColors(
    val containerColor: Color,
    val activeItemColor: Color,
    val inactiveItemColor: Color,
)

@Composable
fun NavigationBarWidget(
    actionHandler: AppActionHandler,
    items: List<Route.BarScreen>,
    colors: NavigationBarColors,
    isNavigationBarItemSelected: (Route.BarScreen) -> Boolean,
    modifier: Modifier = Modifier,
    showLabels: Boolean = isPortrait(),
) {
    Surface(
        color = colors.containerColor,
        shadowElevation = dimensionResource(R.dimen.navbar_elevation),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .defaultMinSize(minHeight = if (showLabels) 80.dp else 50.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { item ->
                Box(
                    modifier = Modifier.weight(1f),
                ) {
                    val isSelected by remember {
                        derivedStateOf { isNavigationBarItemSelected(item) }
                    }
                    val color by remember {
                        derivedStateOf {
                            if (isSelected) {
                                colors.activeItemColor
                            } else {
                                colors.inactiveItemColor
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .widthIn(min = 80.dp)
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                actionHandler.handleAction(AppAction.NavigateToRoute(item))
                            }
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            painter = painterResource(item.iconRes),
                            tint = color,
                            contentDescription = "",
                            modifier = Modifier.size(20.dp),
                        )
                        if (showLabels) {
                            Spacer(Modifier.size(4.dp))
                            Text(
                                text = stringResource(item.titleRes),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = color,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun NavigationBarWidgetPreviewOne() {
    MaterialTheme {
        NavigationBarWidget(
            actionHandler = appActionHandlerStub,
            items = listOf(Route.InfoScreen, Route.HistoryScreen, Route.SettingsScreen),
            colors = NavigationBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                activeItemColor = MaterialTheme.colorScheme.primary,
                inactiveItemColor = MaterialTheme.colorScheme.secondary,
            ),
            isNavigationBarItemSelected = { it is Route.InfoScreen },
        )
    }
}
