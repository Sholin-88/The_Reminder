package com.sholin.the_reminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.sholin.the_reminder.composeScreens.CreateReminder
import com.sholin.the_reminder.ui.theme.The_ReminderTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CommonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            The_ReminderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CreateReminder(viewModel, innerPadding)
                }
            }
        }
    }
}
