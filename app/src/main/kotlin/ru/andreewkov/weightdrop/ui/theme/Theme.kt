package ru.andreewkov.weightdrop.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Grey = Color(0xFFEDECFF)
val Peach = Color(0xFFF8D083)
val LightPeach = Color(0xFFFFEED5)
val Dark = Color(0xFF0B0518)

private val colorScheme = darkColorScheme(
    primary = Grey,
    secondary = Peach,
    tertiary = LightPeach,
    background = Dark,
)

@Composable
fun WeightDropTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}