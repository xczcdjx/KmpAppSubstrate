package com.djx.kmpappsubstrate.web

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual fun createProxyHttpClient(): HttpClient = HttpClient(Darwin)
