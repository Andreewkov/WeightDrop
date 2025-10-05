package ru.andreewkov.weightdrop.domain.weighting

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ru.andreewkov.weightdrop.domain.model.HistoryBlock
import ru.andreewkov.weightdrop.domain.model.Weighting
import java.time.LocalDate
import java.time.Month
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class CalculateHistoryBlocksUseCaseTest {

    @Test
    fun no_calculate_block_from_zero_weighting() {
        val useCase = CalculateHistoryBlocksUseCase(Dispatchers.Default)

        val result = runBlocking {
            useCase.invoke(weightings = listOf())
        }

        assertEquals(true, result.isFailure)
    }

    @Test
    fun calculate_block_from_one_weighting() {
        val useCase = CalculateHistoryBlocksUseCase(Dispatchers.Default)
        val weightings = listOf(d_25_10_25_w_93_7)

        val expected = listOf(
            HistoryBlock(
                header = HistoryBlock.Header(
                    month = Month.OCTOBER,
                    year = 2025,
                    diff = 0.0f,
                ),
                weightingItems = listOf(
                    HistoryBlock.Item(
                        weighting = d_25_10_25_w_93_7,
                        diff = 0.0f,
                    ),
                ),
            ),
        )
        val result = runBlocking {
            useCase.invoke(weightings = weightings)
        }

        assertEquals(true, result.isSuccess)
        assertEquals(expected, result.getOrThrow())
    }

    @Test
    fun calculate_block_from_two_weightings_one_month() {
        val useCase = CalculateHistoryBlocksUseCase(Dispatchers.Default)
        val weightings = listOf(
            d_25_10_25_w_93_7,
            d_24_10_25_w_92_8,
        )

        val expected = listOf(
            HistoryBlock(
                header = HistoryBlock.Header(
                    month = Month.OCTOBER,
                    year = 2025,
                    diff = 0.9f,
                ),
                weightingItems = listOf(
                    HistoryBlock.Item(
                        weighting = d_25_10_25_w_93_7,
                        diff = 0.9f,
                    ),
                    HistoryBlock.Item(
                        weighting = d_24_10_25_w_92_8,
                        diff = 0.0f,
                    ),
                ),
            ),
        )
        val result = runBlocking {
            useCase.invoke(weightings = weightings)
        }

        assertEquals(true, result.isSuccess)
        assertEquals(expected, result.getOrThrow())
    }

    @Test
    fun calculate_block_from_many_weightings_one_month() {
        val useCase = CalculateHistoryBlocksUseCase(Dispatchers.Default)
        val weightings = listOf(
            d_25_10_25_w_93_7,
            d_24_10_25_w_92_8,
            d_20_10_25_w_95_0,
            d_04_10_25_w_92_8,
            d_01_10_25_w_91_9,
        )

        val expected = listOf(
            HistoryBlock(
                header = HistoryBlock.Header(
                    month = Month.OCTOBER,
                    year = 2025,
                    diff = 1.8f,
                ),
                weightingItems = listOf(
                    HistoryBlock.Item(
                        weighting = d_25_10_25_w_93_7,
                        diff = 0.9f,
                    ),
                    HistoryBlock.Item(
                        weighting = d_24_10_25_w_92_8,
                        diff = -2.2f,
                    ),
                    HistoryBlock.Item(
                        weighting = d_20_10_25_w_95_0,
                        diff = 2.2f,
                    ),
                    HistoryBlock.Item(
                        weighting = d_04_10_25_w_92_8,
                        diff = 0.9f,
                    ),
                    HistoryBlock.Item(
                        weighting = d_01_10_25_w_91_9,
                        diff = 0.0f,
                    ),
                ),
            ),
        )
        val result = runBlocking {
            useCase.invoke(weightings = weightings)
        }

        assertEquals(true, result.isSuccess)
        assertEquals(expected, result.getOrThrow())
    }

    @Test
    fun calculate_block_from_many_weightings_multi_month() {
        val useCase = CalculateHistoryBlocksUseCase(Dispatchers.Default)
        val weightings = listOf(
            d_25_10_25_w_93_7,
            d_24_10_25_w_92_8,
            d_20_10_25_w_95_0,
            d_04_10_25_w_92_8,
            d_01_10_25_w_91_9,
            d_21_09_25_w_94_2,
            d_19_09_25_w_94_3,
            d_08_08_25_w_97_0,
            d_01_03_25_w_97_1,
        )

        val expected = listOf(
            HistoryBlock(
                header = HistoryBlock.Header(
                    month = Month.OCTOBER,
                    year = 2025,
                    diff = -0.5f,
                ),
                weightingItems = listOf(
                    HistoryBlock.Item(
                        weighting = d_25_10_25_w_93_7,
                        diff = 0.9f,
                    ),
                    HistoryBlock.Item(
                        weighting = d_24_10_25_w_92_8,
                        diff = -2.2f,
                    ),
                    HistoryBlock.Item(
                        weighting = d_20_10_25_w_95_0,
                        diff = 2.2f,
                    ),
                    HistoryBlock.Item(
                        weighting = d_04_10_25_w_92_8,
                        diff = 0.9f,
                    ),
                    HistoryBlock.Item(
                        weighting = d_01_10_25_w_91_9,
                        diff = -2.3f,
                    ),
                ),
            ),
            HistoryBlock(
                header = HistoryBlock.Header(
                    month = Month.SEPTEMBER,
                    year = 2025,
                    diff = -2.8f,
                ),
                weightingItems = listOf(
                    HistoryBlock.Item(
                        weighting = d_21_09_25_w_94_2,
                        diff = -0.1f,
                    ),
                    HistoryBlock.Item(
                        weighting = d_19_09_25_w_94_3,
                        diff = -2.7f,
                    ),
                ),
            ),
            HistoryBlock(
                header = HistoryBlock.Header(
                    month = Month.AUGUST,
                    year = 2025,
                    diff = -0.1f,
                ),
                weightingItems = listOf(
                    HistoryBlock.Item(
                        weighting = d_08_08_25_w_97_0,
                        diff = -0.1f,
                    ),
                ),
            ),
            HistoryBlock(
                header = HistoryBlock.Header(
                    month = Month.MARCH,
                    year = 2025,
                    diff = 0.0f,
                ),
                weightingItems = listOf(
                    HistoryBlock.Item(
                        weighting = d_01_03_25_w_97_1,
                        diff = 0.0f,
                    ),
                ),
            ),
        )
        val result = runBlocking {
            useCase.invoke(weightings = weightings)
        }

        assertEquals(true, result.isSuccess)
        assertEquals(expected, result.getOrThrow())
    }
}

private val d_25_10_25_w_93_7 = Weighting(93.7f, LocalDate.of(2025, 10, 25))
private val d_24_10_25_w_92_8 = Weighting(92.8f, LocalDate.of(2025, 10, 24))
private val d_20_10_25_w_95_0 = Weighting(95.0f, LocalDate.of(2025, 10, 20))
private val d_04_10_25_w_92_8 = Weighting(92.8f, LocalDate.of(2025, 10, 4))
private val d_01_10_25_w_91_9 = Weighting(91.9f, LocalDate.of(2025, 10, 1))
private val d_21_09_25_w_94_2 = Weighting(94.2f, LocalDate.of(2025, 9, 21))
private val d_19_09_25_w_94_3 = Weighting(94.3f, LocalDate.of(2025, 9, 19))
private val d_08_08_25_w_97_0 = Weighting(97.0f, LocalDate.of(2025, 8, 8))
private val d_01_03_25_w_97_1 = Weighting(97.1f, LocalDate.of(2025, 3, 1))
