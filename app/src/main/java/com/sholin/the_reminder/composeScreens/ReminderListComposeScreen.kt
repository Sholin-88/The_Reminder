package com.sholin.the_reminder.composeScreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sholin.the_reminder.CommonViewModel
import com.sholin.the_reminder.R
import com.sholin.the_reminder.Reminder
import com.sholin.the_reminder.composeComponents.SingleReminderItem

@Composable
fun ReminderList(viewModel: CommonViewModel){
    val reminderList = viewModel.reminderList.collectAsState()
    val context= LocalContext.current
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
        items(reminderList.value.size, key = { reminderList.value[it].id }) { item ->
            val reminder = reminderList.value[item]
            Log.d("Sholinaaaa","${reminder.header}")
            SingleReminderItem(reminder, onCheckedChange = {
                viewModel.updateAlarm(reminder,it)
            })
        }

    }

}
