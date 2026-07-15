package com.djx.kmpappsubstrate.web

import io.ktor.client.HttpClient

expect fun createProxyHttpClient(): HttpClient
