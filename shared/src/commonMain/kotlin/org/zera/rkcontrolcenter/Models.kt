package org.zera.rkcontrolcenter

import kotlinx.serialization.Serializable

@Serializable
data class RunasEstoque(
    val galhoAntigo: Int = 0,
    val particulaLuz: Int = 0,
    val armaduraDullahan: Int = 0,
    val cabeloAzul: Int = 0,
    val garraLobo: Int = 0,
    val ouro: Int = 0,
    val denteOgre: Int = 0
)

@Serializable
data class Personagem(
    val nome: String,
    val classe: String = "Cavaleiro Rúnico",
    var zeny: Long = 0L,
    val estoque: RunasEstoque = RunasEstoque(),
    val checksDiarios: Map<String, Boolean> = mapOf()
)
@Serializable
data class PainelData(
    var perfilAtivo: String = "Principal",
    val perfis: MutableMap<String, Personagem> = mutableMapOf(
        "Principal" to Personagem(nome = "Principal")
    )
)