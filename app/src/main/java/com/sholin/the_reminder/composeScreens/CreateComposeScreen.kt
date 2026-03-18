@file:OptIn(ExperimentalMaterial3Api::class)

package com.sholin.the_reminder.composeScreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sholin.the_reminder.CommonViewModel
import com.sholin.the_reminder.R
import com.sholin.the_reminder.Utils
import com.sholin.the_reminder.ui.theme.ComposeTypography
import com.sholin.the_reminder.ui.theme.Typography
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateReminder(viewModel: CommonViewModel, innerPaddingValues: PaddingValues) {
    val focusManager = LocalFocusManager.current
    val keyboardManager1 = LocalSoftwareKeyboardController.current
    var showPicker by remember { mutableStateOf(false) }

    val daysOfWeek = remember {
        val monday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        (0..6).map { monday.plusDays(it.toLong()) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = innerPaddingValues.calculateTopPadding())
            .background(color = colorResource(R.color.white)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically)
                    .padding(start = dimensionResource(R.dimen.activity_margin)),
                text = "Reminder",
                color = colorResource(R.color.black),
                style = ComposeTypography.header
            )

            IconButton(onClick = { viewModel.insertData() },
                enabled = viewModel.isSaveEnabled) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Add",
                    tint = colorResource(R.color.black),
                )
            }
            if (viewModel.isCloseVisible) {
                IconButton(onClick = { viewModel.clearFields() }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = colorResource(R.color.black),
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .height(20.dp),
            thickness = 0.dp,
            color = Color.Transparent
        )

        // Day Selection Row - Modified to show only day names from Monday to Sunday
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(daysOfWeek) { date ->
                val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                val dayId = date.dayOfWeek.value
                val isSelected = viewModel.selectedDayId == dayId

                Column(
                    modifier = Modifier
                        .width(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) colorResource(R.color.purple_200) else Color.LightGray.copy(alpha = 0.3f))
                        .clickable { 
                            viewModel.updateSelectedDay(dayId)
                        }
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = dayName, fontSize = 14.sp, color = if (isSelected) Color.White else Color.Black)
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .height(30.dp),
            thickness = 0.dp,
            color = Color.Transparent
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .size(250.dp, 70.dp)
                .padding(start = 10.dp, end = 10.dp)
                .background(
                    color = colorResource(R.color.white),
                    shape = RoundedCornerShape(12.dp)
                ),
            value = viewModel.header,
            onValueChange = {
                viewModel.header = it
            },
            shape = RoundedCornerShape(12.dp),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardManager1?.hide()
                    focusManager.clearFocus()
                },
            ),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black),
            textStyle = Typography.labelMedium,
            singleLine = true,
            label = { Text(text = "Add Header") }
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            thickness = 10.dp,
            color = colorResource(R.color.white)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .size(250.dp, 100.dp)
                .padding(start = 10.dp, end = 10.dp)
                .background(
                    color = colorResource(R.color.white),
                    shape = RoundedCornerShape(12.dp)
                ),
            value = viewModel.description,
            onValueChange = {
                viewModel.description = it
            },
            shape = RoundedCornerShape(12.dp),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardManager1?.hide()
                    focusManager.clearFocus()
                },
            ),
            colors = OutlinedTextFieldDefaults.colors(Color.Black, Color.Black),
            textStyle = Typography.labelMedium,
            singleLine = false,
            label = { Text(text = "Add Description") },
            maxLines = 4)

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            thickness = 5.dp,
            color = colorResource(R.color.white)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { showPicker = true }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date",
                    tint = colorResource(R.color.teal_200),
                )
            }

            if (viewModel.selectedDateEpoch.text.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .align(alignment = Alignment.CenterVertically)
                        .padding(start = dimensionResource(R.dimen.activity_margin)),
                    text = Utils.formatMillis(viewModel.selectedDateEpoch.text.toLong()),
                    color = colorResource(R.color.black),
                    style = ComposeTypography.bodyMedium
                )
            }
        }

        if (showPicker) {
            Utils.DateTimePicker(
                onDateTimeSelected = { millis ->
                    if (millis > 0) {
                        viewModel.updateSelectedDate(millis)
                    }
                    showPicker = false
                }
            )
        }
    }
}
