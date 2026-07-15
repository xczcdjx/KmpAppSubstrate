package com.djx.kmpappsubstrate.web

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.server.request.httpMethod
import io.ktor.server.request.receiveChannel
import io.ktor.server.request.uri
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray

private const val REMOTE_ORIGIN = "https://transient.cloud"

private val proxyClient = HttpClient(CIO) {
    followRedirects = false
}

private val requestHeadersToSkip = setOf(
    HttpHeaders.Host.lowercase(),
    HttpHeaders.ContentLength.lowercase(),
    HttpHeaders.Connection.lowercase(),
)

private val responseHeadersToSkip = setOf(
    HttpHeaders.ContentLength.lowercase(),
    HttpHeaders.TransferEncoding.lowercase(),
    HttpHeaders.Connection.lowercase(),
)

/** Proxies the frontend's relative VITE_BASEURL and VITE_UPLOAD_BASEURL paths. */
fun Route.installApiProxy() {
    proxy("/manager")
    proxy("/static")
}

private fun Route.proxy(prefix: String) {
    route("$prefix/{path...}") {
        handle {
            val requestBody = call.receiveChannel().readRemaining().readByteArray()
            val upstream = proxyClient.request(REMOTE_ORIGIN + call.request.uri) {
                method = call.request.httpMethod
                call.request.headers.forEach { name, values ->
                    if (name.lowercase() !in requestHeadersToSkip) {
                        values.forEach { headers.append(name, it) }
                    }
                }
                if (requestBody.isNotEmpty()) setBody(requestBody)
            }

            upstream.headers.forEach { name, values ->
                if (name.lowercase() !in responseHeadersToSkip) {
                    values.forEach { call.response.headers.append(name, it, safeOnly = false) }
                }
            }
            call.respondBytes(
                bytes = upstream.bodyAsChannel().readRemaining().readByteArray(),
                status = upstream.status,
            )
        }
    }
}
