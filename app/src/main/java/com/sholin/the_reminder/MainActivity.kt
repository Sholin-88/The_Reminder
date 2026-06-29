package com.sholin.the_reminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sholin.the_reminder.presentation.screens.MainScreen
import com.sholin.the_reminder.presentation.viewmodel.CommonViewModel
import com.sholin.the_reminder.presentation.viewmodel.IdealWeightViewModel
import com.sholin.the_reminder.ui.theme.The_ReminderTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CommonViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = application as TheReminderApp
                return CommonViewModel(app, app.reminderUseCases) as T
            }
        }
    }
    private val idealWeightViewModel: IdealWeightViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = application as TheReminderApp
                return IdealWeightViewModel(app, app.calculateIdealWeightUseCase) as T
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            The_ReminderTheme {
                MainScreen(viewModel,idealWeightViewModel)
            }
        }
    }
}
