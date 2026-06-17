package com.djx.kmpappsubstrate.export

import platform.Foundation.NSBundle

actual object LocalWeb {
    actual fun index(): String {
        val path = NSBundle.mainBundle.pathForResource(
            "index",
            ofType = "html",
            inDirectory = "www"
        )
        println(NSBundle.mainBundle.bundlePath)
        println("file://$path")
        return path?.let { "file://$it" } ?: "about:blank"
    }
}