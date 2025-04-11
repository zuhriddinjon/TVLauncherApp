package com.example.tvlauncherapp.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.example.tvlauncherapp.R
import com.example.tvlauncherapp.ui.components.AppItem
import com.example.tvlauncherapp.ui.components.SearchBar
import com.example.tvlauncherapp.ui.components.TvButton
import com.example.tvlauncherapp.ui.screen.model.getInstalledApps
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LauncherScreen(onThemeToggle: () -> Unit) {
    val context = LocalContext.current
    var isLauncherEnabled by remember { mutableStateOf(true) }
    var showSettings by remember { mutableStateOf(false) }
    var showFavorites by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    var apps by remember { mutableStateOf(getInstalledApps(context)) }
    val filteredApps = apps.filter {
        it.name.contains(searchQuery, ignoreCase = true) && (!showFavorites || it.isFavorite)
    }

    val timeFormatter = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    var currentTime by remember { mutableStateOf(timeFormatter.format(Date())) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            while (true) {
                currentTime = timeFormatter.format(Date())
                delay(1000)
            }
        }
    }

    if (showSettings) {
        SettingsScreen(
            onBack = { showSettings = false },
            onThemeToggle = onThemeToggle
        )
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = currentTime,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row {
                        TvButton(
                            onClick = { isLauncherEnabled = !isLauncherEnabled },
                            text = if (isLauncherEnabled) "O‘chirish" else "Yoqish",
                            modifier = Modifier
                                .focusable()
                                .padding(end = 8.dp)
                        )
                        TvButton(
                            onClick = { showFavorites = !showFavorites },
                            text = if (showFavorites) "Barcha ilovalar" else "Sevimlilar",
                            modifier = Modifier
                                .focusable()
                                .padding(end = 8.dp)
                        )
                        TvButton(
                            onClick = {
                                showSettings = true
                            },
                            text = "Sozlamalar",
                        )
                    }
                }

                if (isLauncherEnabled) {
                    SearchBar(
                        onSearch = { query -> searchQuery = query }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredApps.mapNotNull { if (it.packageName.contains(context.packageName)) null else it }) { app ->
                            AppItem(
                                app = app,
                                onToggleFavorite = {
                                    apps = apps.map {
                                        if (it.packageName == app.packageName) it.copy(isFavorite = !it.isFavorite)
                                        else it
                                    }
                                }
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Launcher o‘chirilgan",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

