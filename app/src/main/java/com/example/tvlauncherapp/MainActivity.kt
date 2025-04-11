package com.example.tvlauncherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.tvlauncherapp.ui.screen.LauncherScreen
import com.example.tvlauncherapp.ui.theme.TVLauncherAppTheme
import com.example.tvlauncherapp.util.DefaultLauncherHelper

class MainActivity : ComponentActivity() {

    private val defaultLauncherHelper: DefaultLauncherHelper by lazy { DefaultLauncherHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validateDefaultLauncher()

        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }
            TVLauncherAppTheme(isInDarkTheme = isDarkTheme) {
                LauncherScreen(
                    onThemeToggle = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }

    private fun validateDefaultLauncher() {
        if (!defaultLauncherHelper.isDefaultLauncher() && defaultLauncherHelper.canRequestDefaultLauncher()) {
            val intent = defaultLauncherHelper.requestDefaultLauncherIntent()
            if (intent != null) startActivity(intent, null)
        }
    }
}
