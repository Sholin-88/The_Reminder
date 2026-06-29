@file:OptIn(ExperimentalMaterial3Api::class)

package com.sholin.the_reminder.presentation.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sholin.the_reminder.R
import com.sholin.the_reminder.navigation.Screen
import com.sholin.the_reminder.presentation.viewmodel.CommonViewModel
import com.sholin.the_reminder.ui.theme.ComposeTypography
import com.sholin.the_reminder.ui.theme.Typography
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateReminder(viewModel: CommonViewModel, navController: NavController,) {
    var showWeekTimePicker by remember { mutableStateOf(false) }

    val daysOfWeek = remember {
        val monday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        (0..6).map { monday.plusDays(it.toLong()) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
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
                text = "Recurring Reminder",
                color = colorResource(R.color.black),
                style = ComposeTypography.header
            )

            IconButton(onClick = {
                viewModel.insertData()
                    navController.navigate(Screen.List.route)
                                 },
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

        HorizontalDivider(modifier = Modifier.height(20.dp), thickness = 0.dp, color = Color.Transparent)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().height(70.dp).padding(horizontal = 10.dp),
            value = viewModel.header,
            onValueChange = { viewModel.header = it },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black),
            textStyle = Typography.labelMedium,
            singleLine = true,
            label = { Text(text = "Add Header") }
        )

        HorizontalDivider(modifier = Modifier.height(10.dp), thickness = 0.dp, color = Color.Transparent)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().height(100.dp).padding(horizontal = 10.dp),
            value = viewModel.description,
            onValueChange = { viewModel.description = it },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(Color.Black, Color.Black),
            textStyle = Typography.labelMedium,
            label = { Text(text = "Add Description") },
            maxLines = 4
        )

        HorizontalDivider(modifier = Modifier.height(20.dp), thickness = 0.dp, color = Color.Transparent)

        // Day Selection Row
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            LazyRow(
                modifier = Modifier.weight(1f).wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(daysOfWeek) { date ->
                    val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    val dayId = date.dayOfWeek.value
                    val isSelected = viewModel.selectedDayIds.contains(dayId)

                    Column(
                        modifier = Modifier
                            .width(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) colorResource(R.color.purple_200) else Color.LightGray.copy(alpha = 0.3f))
                            .clickable { viewModel.toggleDaySelection(dayId) }
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = dayName, fontSize = 12.sp, color = if (isSelected) Color.White else Color.Black)
                    }
                }
            }

            // Dedicated Time Picker for Week Days
            IconButton(onClick = { showWeekTimePicker = true }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Set Time for Days",
                    tint = if (viewModel.selectedDaysTime != null) colorResource(R.color.purple_200) else Color.Gray
                )
            }
        }

        if (viewModel.selectedDaysTime != null && viewModel.selectedDayIds.isNotEmpty()) {
            Text(
                text = "Every selected day at ${viewModel.selectedDaysTime}",
                style = Typography.labelSmall,
                color = colorResource(R.color.purple_500),
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        // Dialogs
        if (showWeekTimePicker) {
            android.app.TimePickerDialog(
                androidx.compose.ui.platform.LocalContext.current,
                { _, hourOfDay, minute ->
                    viewModel.updateSelectedDaysTime(hourOfDay, minute)
                    showWeekTimePicker = false
                },
                12, 0, false
            ).show()
        }
    }
}
