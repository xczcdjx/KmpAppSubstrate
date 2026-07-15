package com.djx.kmpappsubstrate.views.local

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.djx.kmpappsubstrate.web.LocalWebServer
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState

@Composable
fun LocalScreenOld(back: () -> Unit = {}) {
    LocalWebServer.start()
    println("indexUrl${LocalWebServer.indexUrl}")
    val webViewState = rememberWebViewState(url = LocalWebServer.indexUrl)
    val navigator = rememberWebViewNavigator()
    LaunchedEffect(Unit) {
        webViewState.webSettings.zoomLevel = 1.0

        webViewState.webSettings.apply {
            allowUniversalAccessFromFileURLs = true
            zoomLevel = 1.0
            logSeverity = KLogSeverity.Debug
            backgroundColor = Color.White
            androidWebSettings.apply {
                isAlgorithmicDarkeningAllowed = true
                safeBrowsingEnabled = true

                allowFileAccess = true   // ⭐必须打开
                isJavaScriptEnabled=true
            }
            iOSWebSettings.apply {
                backgroundColor = Color.White
                underPageBackgroundColor = Color.White
            }
        }
    }
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text("Local Webview")
            },
            navigationIcon = {
                IconButton({
                    back()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            },
            actions = {
                if (navigator.canGoBack) {
                    IconButton(onClick = { navigator.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            })
    }) { paddingValues ->
        Column(Modifier.padding(paddingValues).fillMaxSize()) {
            Text(text = webViewState.pageTitle ?: "null")

            val loadingState = webViewState.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    progress = { loadingState.progress },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            WebView(
                state = webViewState,
                navigator = navigator,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),

            )
        }
    }
}
