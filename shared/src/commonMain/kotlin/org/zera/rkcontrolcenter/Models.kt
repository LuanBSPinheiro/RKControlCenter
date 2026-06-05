package org.zera.rkcontrolcenter

import kotlinx.serialization.Serializable

@Serializable
data class RunasEstoque(
    var galhoAntigo: Int = 0,
    var particulaLuz: Int = 0,
    var armaduraDullahan: Int = 0,
    var cabeloAzul: Int = 0,
    var garraLobo: Int = 0,
    var ouro: Int = 0,
    var denteOgre: Int = 0
)

@Serializable
data class Personagem(
    val nome: String,
    val classe: String = "Cavaleiro Rúnico",
    var zeny: Long = 0L,
    val estoque: RunasEstoque = RunasEstoque(),
    val checksDiarios: MutableMap<String, Boolean> = mutableMapOf()
)

@Serializable
data class PainelData(
    var perfilAtivo: String = "Principal",
    val perfis: MutableMap<String, Personagem> = mutableMapOf(
        "Principal" to Personagem(nome = "Principal")
    )
)