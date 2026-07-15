package com.djx.kmpappsubstrate.web

expect object LocalWebServer {
    val indexUrl: String

    fun start()

    fun stop()
}
