package com.djx.kmpappsubstrate

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform