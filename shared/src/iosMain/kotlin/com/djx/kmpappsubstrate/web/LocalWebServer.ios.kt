@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.djx.kmpappsubstrate.web

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kmpappsubstrate.shared.generated.resources.Res

actual object LocalWebServer {
    private const val HOST = "127.0.0.1"
    private const val PORT = 8765

    actual val indexUrl: String = "http://$HOST:$PORT/index.html"

    private var stopServer: (() -> Unit)? = null

    actual fun start() {
        if (stopServer != null) return

        val server = embeddedServer(CIO, host = HOST, port = PORT) {
            routing {
                installApiProxy()
                get("/{path...}") {
                    val requestPath = call.parameters.getAll("path")
                        ?.joinToString("/")
                        ?.ifBlank { "index.html" }
                        ?: "index.html"

                    if (requestPath.split('/').any { it == ".." }) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }

                    val bytes = try {
                        Res.readBytes("files/web/$requestPath")
                    } catch (_: Exception) {
                        null
                    }
                    if (bytes == null) {
                        call.respond(HttpStatusCode.NotFound)
                    } else {
                        call.respondBytes(bytes, contentTypeFor(requestPath))
                    }
                }
            }
        }
        server.start(wait = false)
        stopServer = { server.stop(500, 1_000) }
    }

    actual fun stop() {
        stopServer?.invoke()
        stopServer = null
    }

    private fun contentTypeFor(path: String): ContentType = when (path.substringAfterLast('.', "")) {
        "html" -> ContentType.Text.Html
        "css" -> ContentType.Text.CSS
        "js", "mjs" -> ContentType.Application.JavaScript
        "json" -> ContentType.Application.Json
        "svg" -> ContentType.Image.SVG
        "png" -> ContentType.Image.PNG
        "jpg", "jpeg" -> ContentType.Image.JPEG
        "gif" -> ContentType.Image.GIF
        "webp" -> ContentType.parse("image/webp")
        "woff" -> ContentType.parse("font/woff")
        "woff2" -> ContentType.parse("font/woff2")
        else -> ContentType.Application.OctetStream
    }
}
