package com.djx.kmpappsubstrate.router

sealed class Routes(val route: String) {
    object Index : Routes("Index")
    object Local : Routes("LocalWebView")
    object Remote : Routes("RemoteWebView")
}