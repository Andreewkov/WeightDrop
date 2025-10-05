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
    fun readStringFromContext_LocalizedString() {
        val useCase = CalculateHistoryBlocksUseCase(Dispatchers.Default)
        val weightings = listOf(
            Weighting(93.7f, LocalDate.of(2025, 10, 25))
        )

        val expected = listOf(
            HistoryBlock(
                header = HistoryBlock.Header(
                    month = Month.OCTOBER,
                    year = 2025,
                    diff = 0.0f,
                ),
                weightingItems = listOf(
                    HistoryBlock.Item(
                        weighting = Weighting(93.7f, LocalDate.of(2025, 10, 25)),
                        diff = 0.0f,
                    )
                )
            )
        )
        val result = runBlocking {
            useCase.invoke(weightings = weightings)
        }

        assertEquals(true, result.isSuccess)
        assertEquals(expected, result.getOrThrow())
    }
}
