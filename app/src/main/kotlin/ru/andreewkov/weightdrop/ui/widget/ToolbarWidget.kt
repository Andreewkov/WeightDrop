package ru.andreewkov.weightdrop.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.ui.screen.AppAction
import ru.andreewkov.weightdrop.ui.screen.AppActionHandler
import ru.andreewkov.weightdrop.ui.screen.Screen

data class ToolbarWidgetColors(
    val containerColor: Color,
    val contentColor: Color,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarWidget(
    currentRoute: String?,
    actionHandler: AppActionHandler,
    colors: ToolbarWidgetColors,
) {
    CenterAlignedTopAppBar(
        navigationIcon = {},
        title = {
            Text(
                text = stringResource(Screen.findScreen(currentRoute).titleRes),
                style = TextStyle(
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                ),
            )
        },
        colors = TopAppBarColors(
            containerColor = colors.containerColor,
            scrolledContainerColor = colors.containerColor,
            navigationIconContentColor = colors.contentColor,
            titleContentColor = colors.contentColor,
            actionIconContentColor = colors.contentColor,
        ),
        windowInsets = WindowInsets.statusBars,
        actions = {
            Icon(
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { actionHandler.handleAction(AppAction.OnClickAdd) }
                    .padding(8.dp),
            )
        },
        modifier = Modifier.shadow(
            elevation = dimensionResource(R.dimen.toolbar_elevation),
        )
    )
}