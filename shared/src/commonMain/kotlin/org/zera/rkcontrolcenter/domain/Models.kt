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
    val denteOgre: Int = 0,
    val pergaminhoAntigo: Int = 0,
    val armaduraDestruida: Int = 0,
    val caninoDragao: Int = 0,
    val corrente: Int = 0,
    val runaBerkana: Int = 0,
    val runaThurisaz: Int = 0,
    val runaLuxanima: Int = 0,
    val runaOthila: Int = 0,
    val runaNauthiz: Int = 0,
    val runaWyrd: Int = 0,
    val precosMercado: Map<String, Int> = emptyMap()
)

@Serializable
data class AtividadeFarm(
    val id: String,
    val name: String,
    val rota: String,
    val loot: String,
    val test: Boolean = false,
    val monstro: String = ""
)

@Serializable
data class Personagem(
    val nome: String,
    val classe: String = "Cavaleiro Rúnico",
    val zeny: Long = 0L,
    val estoque: RunasEstoque = RunasEstoque(),
    val checksDiarios: Map<String, Boolean> = mapOf(),
    val atividadesCustom: List<AtividadeFarm> = listOf()
)

@Serializable
data class PainelData(
    val perfilAtivo: String = "Principal",
    val perfis: Map<String, Personagem> = mapOf(
        "Principal" to Personagem(nome = "Principal")
    )
)
