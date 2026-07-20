package com.sholin.the_reminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.sholin.the_reminder.Firebase.FirebaseProvider
import com.sholin.the_reminder.screens.MainScreen
import com.sholin.the_reminder.ui.theme.The_ReminderTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseProvider: FirebaseProvider

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val apps = FirebaseApp.getApps(this)
        if (apps.isNotEmpty()) {
            Log.d("FirebaseCheck", "Firebase initialized successfully: ${apps[0].name}")
        } else {
            Log.e("FirebaseCheck", "Firebase NOT initialized!")
        }

        firebaseProvider.log("MainActivity created")
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
                MainScreen()
            }
        }
    }
}
