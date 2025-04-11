package com.example.tvlauncherapp.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil3.compose.rememberAsyncImagePainter
import com.example.tvlauncherapp.ui.screen.model.AppInfo


@Composable
fun AppItem(
    app: AppInfo,
    onToggleFavorite: (AppInfo) -> Unit
) {
    val context = LocalContext.current
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .width(120.dp)
            .focusable()
            .scale(if (isFocused) 1.1f else 1.0f)
            .border(
                width = if (isFocused) 2.dp else 0.dp,
                color = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
            )
            .animateContentSize(animationSpec = tween(200))
            .clickable {

                val intent = context.packageManager.getLaunchIntentForPackage(app.packageName)
                intent?.let { intentToStart ->
                    context.startActivity(intentToStart, null)
                }
            }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = app.icon),
            contentDescription = app.name,
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 8.dp)
        )
        Text(
            text = app.name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (app.isFavorite) "♥" else "♡",
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.clickable { onToggleFavorite(app) }
        )
    }
}