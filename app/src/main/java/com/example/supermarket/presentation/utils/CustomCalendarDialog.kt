package com.example.supermarket.presentation.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.supermarket.presentation.ui.theme.Green
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCalendarDialog(
    onDismiss: () -> Unit,
    onDateSelected: (day: Int, month: Int, year: Int) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            delay(100)

            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                timeInMillis = millis
            }
            onDateSelected(
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR)
            )
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            DatePicker(
                state = datePickerState,
                title = null,
                headline = null,
                showModeToggle = false,
                modifier = Modifier.padding(top = 8.dp),
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    dayContentColor = Color.Black,
                    selectedDayContainerColor = Green,
                    selectedDayContentColor = Color.White,
                    todayDateBorderColor = Green,
                    todayContentColor = Color.Black,
                    currentYearContentColor = Color.Black,
                    selectedYearContainerColor = Green,
                    selectedYearContentColor = Color.White,
                    weekdayContentColor = Color.Gray,
                    navigationContentColor = Color.Black
                )
            )
        }
    }
}