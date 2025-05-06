package ru.andreewkov.weightdrop.ui.screen.add

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.andreewkov.weightdrop.ui.theme.Grey
import ru.andreewkov.weightdrop.ui.theme.Peach
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme
import ru.andreewkov.weightdrop.ui.util.WeightDropPreview
import ru.andreewkov.weightdrop.ui.util.observe
import ru.andreewkov.weightdrop.ui.widget.WeightPickerNum
import ru.andreewkov.weightdrop.ui.widget.WeightPickerWidget
import ru.andreewkov.weightdrop.ui.widget.rememberWeightPickerWidgetState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun AddScreenUI() {
    val viewModel: AddViewModel = hiltViewModel()
    val state by viewModel.screenState.collectAsState()

    AddScreenContent1(state.date, state.weight)
}

@Composable
private fun AddScreenContent() {

}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddScreenContent1(
    date: LocalDate,
    weight: Float?,
    modifier: Modifier = Modifier,
) {
    val datePickerColors = DatePickerDefaults.colors(
        containerColor = MaterialTheme.colorScheme.background,
    )

    DatePickerDialog(
        onDismissRequest = {

        },
        confirmButton = {
            Button(
                onClick = {
                    //weightPickerWidgetState.updateValue(WeightPickerNum(76, 2))
                },
                modifier = Modifier
            ) {
                Text(
                    text = "Click me!"
                )
            }
        },
        colors = datePickerColors,
    ) {
        val state = rememberDatePickerState()

        DatePicker(
            state = state,
            colors = datePickerColors,
            showModeToggle = false,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddScreenContent2(
    date: LocalDate,
    weight: Float?,
    modifier: Modifier = Modifier,
) {
    DatePickerDialog(
        onDismissRequest = { },
        confirmButton = {}
    ) {
        val datePickerState = rememberDatePickerState(
            initialDisplayMode = DisplayMode.Picker
        )
        val weightPickerWidgetState = rememberWeightPickerWidgetState(
            num = WeightPickerNum(79, 3)
        )
        val selectedDate = datePickerState.selectedDateMillis?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
        }
        val dateMillis by remember {
            derivedStateOf {
                datePickerState.selectedDateMillis
            }
        }
        LaunchedEffect(dateMillis) {
            Log.d("rrrttt", "dateMillis = $dateMillis")
            //weightPickerWidgetState.updateValue(WeightPickerNum(79, 6))
        }
        Column(
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .weight(1f)
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                    showModeToggle = false,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column {
                WeightPickerWidget(
                    state = weightPickerWidgetState,
                    primaryColor = Grey,
                    secondaryColor = Peach,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                )
                Button(
                    onClick = {
                        weightPickerWidgetState.updateValue(WeightPickerNum(76, 2))
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Click me!"
                    )
                }
            }
        }

        LaunchedEffect(Unit) {
            weightPickerWidgetState.currentValue.observe {
                Log.d("rrrttt", "currentValue = $it")
            }
        }
    }

}

@Composable
@WeightDropPreview
private fun AddScreenContentPreview() {
    WeightDropTheme {
        Scaffold { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                AddScreenContent1(
                    date = LocalDate.now(),
                    weight = 103.2f,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
@WeightDropPreview
private fun AddScreenContentEmpty() {
    WeightDropTheme {
        Scaffold { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                AddScreenContent(
//                    date = LocalDate.now().minusWeeks(24),
//                    weight = null,
//                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}
