package ru.andreewkov.weightdrop.domain.weighting

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.andreewkov.weightdrop.domain.di.HistoryDispatcherQualifier
import ru.andreewkov.weightdrop.domain.model.HistoryBlock
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.utils.roundToDecimals
import java.time.LocalDate
import javax.inject.Inject

class CalculateHistoryBlocksUseCase @Inject constructor(
    @HistoryDispatcherQualifier private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(
        weightings: List<Weighting>,
        previous: Weighting? = null,
    ): Result<List<HistoryBlock>> = withContext(dispatcher) {
        runCatching {
            calculateBlocks(weightings, previous)
        }
    }

    private fun calculateBlocks(
        weightings: List<Weighting>,
        previous: Weighting? = null,
    ): List<HistoryBlock> {
        check(weightings.isNotEmpty())
        val sortedWeightings = weightings.sortedBy { it.date }
        var prevWeight = sortedWeightings.first().value

        val historyItems = sortedWeightings.map { weighting ->
            HistoryBlock.Item(
                weighting = weighting,
                diff = (weighting.value - prevWeight).roundToDecimals(),
            ).also {
                prevWeight = weighting.value
            }
        }

        return buildList {
            var startIndex = 0
            var monthDate = sortedWeightings.first().date
            historyItems.forEachIndexed { index, item ->
                val currentDate = item.weighting.date
                if (currentDate.month != monthDate.month || currentDate.year != monthDate.year) {
                    addHeader(
                        monthDate = monthDate,
                        items = historyItems.subList(startIndex, index),
                        takeHeader = startIndex != 0 || previous?.date?.month != monthDate.month,
                    )

                    monthDate = item.weighting.date
                    startIndex = index
                }
            }

            addHeader(
                monthDate = monthDate,
                items = historyItems.subList(startIndex, historyItems.size),
                takeHeader = startIndex != 0 || previous?.date?.month != monthDate.month,
            )
        }.reversed()
    }

    private fun MutableList<HistoryBlock>.addHeader(
        monthDate: LocalDate,
        items: List<HistoryBlock.Item>,
        takeHeader: Boolean,
    ) {
        add(
            HistoryBlock(
                header = HistoryBlock.Header(
                    month = monthDate.month,
                    year = monthDate.year,
                    diff = items.map { it.diff }.sum().roundToDecimals(),
                ).takeIf { takeHeader },
                weightingItems = items.reversed(),
            ),
        )
    }
}
