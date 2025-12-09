package ru.andreewkov.weightdrop.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Grey = Color(0xFFEDECFF)
val GreyLight = Color(0xFFBDBCCC)
val Peach = Color(0xFFF5D49B)
val PeachLight = Color(0xFFFCF0DF)
val Dark = Color(0xFF0B0518)
val DarkLight = Color(0xFF1E192C)
val Red = Color(0xFFE3212D)

private val colorScheme = darkColorScheme(
    surface = Dark,
    primary = Grey,
    onPrimary = Dark,
    secondary = Peach,
    onSecondary = Dark,
    tertiary = PeachLight,
    onTertiary = Dark,
    background = Dark,
    onSurface = Peach,
    surfaceContainer = Dark,
    surfaceVariant = DarkLight,
    error = Red,
    onError = Grey,
)

@Composable
fun WeightDropTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
