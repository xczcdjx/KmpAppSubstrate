package com.djx.kmpappsubstrate.web

import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.defaultResource
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing

actual object LocalWebServer {
    private const val HOST = "127.0.0.1"
    private const val PORT = 8765

    actual val indexUrl: String = "http://$HOST:$PORT/index.html"

    private var stopServer: (() -> Unit)? = null

    @Synchronized
    actual fun start() {
        if (stopServer != null) return

        val server = embeddedServer(CIO, host = HOST, port = PORT) {
            routing {
                installApiProxy()
                staticResources("/", "www") {
                    defaultResource("index.html", "www")
                }
            }
        }
        server.start(wait = false)
        stopServer = { server.stop(500, 1_000) }
    }

    @Synchronized
    actual fun stop() {
        stopServer?.invoke()
        stopServer = null
    }
}
