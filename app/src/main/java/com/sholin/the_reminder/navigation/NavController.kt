package com.sholin.the_reminder.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sholin.the_reminder.CommonViewModel
import com.sholin.the_reminder.IdealWeightViewModel
import com.sholin.the_reminder.composeScreens.CreateReminder
import com.sholin.the_reminder.composeScreens.FindIdealWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.sholin.the_reminder.composeScreens.ReminderList

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Create : Screen("create", "Add", Icons.Default.Add)
    object List : Screen("list", "Reminders", Icons.AutoMirrored.Filled.List)
    object Weight : Screen("weight", "Ideal Weight", Icons.Default.CheckCircle)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
    padding: PaddingValues,
    commonViewModel: CommonViewModel,
    idealWeightViewModel: IdealWeightViewModel
) {
    NavHost(
        navController = navController,
        modifier = Modifier.padding(padding),
        startDestination = Screen.Create.route
    ) {
        composable(Screen.Create.route) {
            CreateReminder(commonViewModel, navController)
        }
        composable(Screen.List.route) {
            ReminderList(commonViewModel, navController)
        }
        composable(Screen.Weight.route) {
            FindIdealWeight(idealWeightViewModel, navController)
        }
    }
}
