package org.zera.rkcontrolcenter

import kotlinx.serialization.json.Json

expect object StoragePlatform {
    fun salvarString(chave: String, valor: String)
    fun carregarString(chave: String): String?
}

object StorageManager {
    private const val STORAGE_KEY = "rk_latam_tracker_data"
    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun salvar(data: PainelData) {
        val jsonString = jsonConfig.encodeToString(data) // 👈 Deixe simples assim
        StoragePlatform.salvarString(STORAGE_KEY, jsonString)
    }

    fun carregar(): PainelData {
        val jsonString = StoragePlatform.carregarString(STORAGE_KEY)
        return if (jsonString != null) {
            try {
                jsonConfig.decodeFromString<PainelData>(jsonString) // 👈 Deixe simples assim
            } catch (e: Exception) {
                PainelData()
            }
        } else {
            PainelData()
        }
    }
}