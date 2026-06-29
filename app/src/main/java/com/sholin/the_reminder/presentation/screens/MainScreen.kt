package com.sholin.the_reminder.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sholin.the_reminder.navigation.AppNavHost
import com.sholin.the_reminder.navigation.Screen
import com.sholin.the_reminder.presentation.viewmodel.CommonViewModel
import com.sholin.the_reminder.presentation.viewmodel.IdealWeightViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(viewModel: CommonViewModel, idealWeightViewModel: IdealWeightViewModel) {
    val navController = rememberNavController()
    val items = listOf(Screen.Create, Screen.List, Screen.Weight)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
         AppNavHost(
            navController = navController,
            commonViewModel = viewModel,
            padding = innerPadding,
            idealWeightViewModel = idealWeightViewModel
        )
    }
}

