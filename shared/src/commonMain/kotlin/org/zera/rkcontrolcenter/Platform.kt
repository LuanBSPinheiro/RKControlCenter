package org.zera.rkcontrolcenter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform