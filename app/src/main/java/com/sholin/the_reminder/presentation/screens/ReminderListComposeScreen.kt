package com.sholin.the_reminder.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sholin.the_reminder.R
import com.sholin.the_reminder.presentation.components.SingleReminderItem
import com.sholin.the_reminder.presentation.viewmodel.CommonViewModel
import com.sholin.the_reminder.ui.theme.ComposeTypography

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReminderList(viewModel: CommonViewModel, navController: NavController) {
    val reminderList by viewModel.reminderList.collectAsState()
    val scroll = rememberNestedScrollInteropConnection()
    val state = rememberLazyStaggeredGridState()

    if (reminderList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No reminders yet.\nTap 'Add' to create one!",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                style = ComposeTypography.bodyMedium
            )
        }
    } else {
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
            items(reminderList, key = { it.id }) { reminder ->
                SingleReminderItem(reminder,
                    onCheckedChange = { isChecked ->
                        viewModel.updateAlarm(reminder, isChecked)
                    },
                    onDeleted = {
                        viewModel.deleteData(reminder.id)
                    })
            }
        }
    }
}
