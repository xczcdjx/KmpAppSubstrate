package com.djx.kmpappsubstrate.web

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

actual fun createProxyHttpClient(): HttpClient = HttpClient(CIO)
