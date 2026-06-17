package com.djx.kmpappsubstrate.export

actual object LocalWeb {
    actual fun index(): String {
        val input = object {}.javaClass.getResourceAsStream("/www/index.html")!!

        val file = kotlin.io.path.createTempFile("index", ".html").toFile()

        file.outputStream().use { output ->
            input.copyTo(output)
        }

        return file.toURI().toString()
    }
}