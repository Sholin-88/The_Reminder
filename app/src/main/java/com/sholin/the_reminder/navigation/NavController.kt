package com.sholin.the_reminder.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sholin.the_reminder.screens.CreateReminder
import com.sholin.the_reminder.screens.ReminderList
import com.sholin.the_reminder.viewmodel.CommonViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Create : Screen("create", "Add", Icons.Default.Add)
    object List : Screen("list", "Reminders", Icons.AutoMirrored.Filled.List)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        modifier = Modifier.padding(padding),
        startDestination = Screen.List.route
    ) {
        composable(Screen.Create.route) {
            val commonViewModel = hiltViewModel<CommonViewModel>()
            CreateReminder(commonViewModel, navController)
        }
        composable(Screen.List.route) {
            val commonViewModel = hiltViewModel<CommonViewModel>()
            ReminderList(commonViewModel, navController)
        }
    }
}
