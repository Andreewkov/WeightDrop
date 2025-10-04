package ru.andreewkov.weightdrop.util

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    device = Devices.PIXEL,
    showSystemUi = true,
)
@Preview(
    device = "spec:width = 411dp, height = 891dp, orientation = landscape, dpi = 420",
    showSystemUi = true,
)
annotation class WeightDropPreview
