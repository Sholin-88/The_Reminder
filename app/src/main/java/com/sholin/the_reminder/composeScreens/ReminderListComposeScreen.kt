package com.sholin.the_reminder.composeScreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.sholin.the_reminder.CommonViewModel
import com.sholin.the_reminder.R
import com.sholin.the_reminder.alarmManager.AlarmHelper
import com.sholin.the_reminder.composeComponents.SingleReminderItem

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReminderList(viewModel: CommonViewModel){
    val reminderList = viewModel.reminderList.collectAsState()
    val context = LocalContext.current
    val scroll = rememberNestedScrollInteropConnection()
    val state = rememberLazyStaggeredGridState()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 180.dp),
        verticalItemSpacing = 10.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        state = state,
        flingBehavior = ScrollableDefaults.flingBehavior(),
        userScrollEnabled = true,
        modifier = Modifier
            .fillMaxWidth()
            .nestedScroll(scroll)
            .padding(horizontal = 10.dp),
        contentPadding = PaddingValues(
            vertical = dimensionResource(id = R.dimen.activity_margin)
        )
    ) {
        items(reminderList.value, key = { it.id }) { reminder ->
            SingleReminderItem(reminder,
                onCheckedChange = { isChecked ->
                    viewModel.updateAlarm(reminder, isChecked)
                    if (isChecked) {
                        AlarmHelper.setAlarm(
                            context = context,
                            triggerAtMillis = reminder.date.toLong(),
                            requestCode = reminder.id,
                            header = reminder.header,
                            description = reminder.description
                        )
                    } else {
                        AlarmHelper.cancelAlarm(context, reminder.id)
                    }
                },
                onDeleted = {
                    AlarmHelper.cancelAlarm(context, reminder.id)
                    viewModel.deleteData(reminder.id)
                })
        }
    }
}
