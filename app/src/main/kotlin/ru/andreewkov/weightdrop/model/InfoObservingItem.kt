package ru.andreewkov.weightdrop.model

import ru.andreewkov.weightdrop.domain.model.Settings
import ru.andreewkov.weightdrop.domain.model.Weighting

data class InfoObservingItem(
    val weightings: List<Weighting>,
    val settings: Settings,
)
