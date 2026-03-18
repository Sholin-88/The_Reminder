package com.sholin.the_reminder.composeScreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.sholin.the_reminder.CommonViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Create : Screen("create", "Add", Icons.Default.Add)
    object List : Screen("list", "Reminders", Icons.AutoMirrored.Filled.List)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(viewModel: CommonViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Create) }
    val items = listOf(Screen.Create, Screen.List)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (currentScreen) {
            is Screen.Create -> CreateReminder(viewModel, innerPadding)
            is Screen.List -> ReminderListScreen(viewModel, innerPadding)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReminderListScreen(viewModel: CommonViewModel, padding: androidx.compose.foundation.layout.PaddingValues) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(padding)) {
        ReminderList(viewModel)
    }
}
