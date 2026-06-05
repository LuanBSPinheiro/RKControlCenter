package org.zera.rkcontrolcenter

actual object StoragePlatform {
    actual fun salvarString(chave: String, valor: String) {
        // Futuramente podemos amarrar com o SharedPreferences ou DataStore aqui
    }

    actual fun carregarString(chave: String): String? {
        return null
    }
}