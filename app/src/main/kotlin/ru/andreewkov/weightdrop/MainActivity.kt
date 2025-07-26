package ru.andreewkov.weightdrop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ru.andreewkov.weightdrop.ui.route.main.MainAppUI
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(0),
            navigationBarStyle = SystemBarStyle.dark(0),
        )

        setContent {
            WeightDropTheme {
                MainAppUI()
            }
        }
    }
}
