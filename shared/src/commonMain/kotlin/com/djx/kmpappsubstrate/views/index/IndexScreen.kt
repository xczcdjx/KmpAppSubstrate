package com.djx.kmpappsubstrate.views.index

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.djx.kmpappsubstrate.router.Routes

@Composable
fun IndexScreen(modifier: Modifier = Modifier, go: (n: String) -> Unit) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Kotlin Multiplatform App Substrate")
        })
    }) { paddingValues ->
        Column(modifier.padding(paddingValues)) {
            TextButton({
                go(Routes.Remote.route)
            }) {
                Text("Romote WebView Demo")
            }
            TextButton({
                go(Routes.Local.route)
            }) {
                Text("Local WebView Demo")
            }
        }
    }
}