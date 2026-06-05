package org.zera.rkcontrolcenter

import kotlinx.browser.localStorage

actual object StoragePlatform {
    actual fun salvarString(chave: String, valor: String) {
        localStorage.setItem(chave, valor)
    }

    actual fun carregarString(chave: String): String? {
        return localStorage.getItem(chave)
    }
}