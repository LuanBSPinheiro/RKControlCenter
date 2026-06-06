package org.zera.rkcontrolcenter.domain

import org.zera.rkcontrolcenter.RunasEstoque

// Modelo de retorno atualizado para expor os detalhes individuais
data class ResultadoCalculoCraft(
    val faltantes_detalhados: List<Pair<String, Int>>, // Expomos os dados puros (Nome, Qtd Faltante)
    val custoTotalZeny: Long // 64-bit para evitar overflow financeiro
)

object MatrizCraft {

    val receitasRunas = mapOf(
        "Berkana" to listOf("Galho Antigo" to 1, "Armadura Dullahan" to 1),
        "Thurisaz" to listOf("Galho Antigo" to 1, "Cabelo Azul" to 1, "Garra de Lobo" to 1),
        "Luxanima" to listOf("Galho Antigo" to 1, "Partículas de Luz" to 3, "Ouro" to 3), // Sincronizado para bater com as chaves dos cards
        "Othila" to listOf("Partículas de Luz" to 1, "Dente de Ogre" to 1, "Galho Antigo" to 1),
        "Nauthiz" to listOf("Pergaminho Antigo" to 1, "Armadura Destruída" to 1, "Partículas de Luz" to 1, "Galho Antigo" to 1),
        "Wyrd" to listOf("Galho Antigo" to 1, "Partículas de Luz" to 1, "Canino de Dragão" to 1, "Corrente" to 1)
    )

    fun calcularCraft(
        estoque: RunasEstoque,
        receita: List<Pair<String, Int>>,
        quantidadeDesejada: Int
    ): ResultadoCalculoCraft {
        val faltantesListaData = mutableListOf<Pair<String, Int>>()
        var custoAcumuladoZeny = 0L

        if (quantidadeDesejada <= 0) return ResultadoCalculoCraft(emptyList(), 0L)

        receita.forEach { (material, qtdPorRuna) ->
            val qtdTotalNecessaria = qtdPorRuna * quantidadeDesejada

            // Resolve as quantidades usando as strings limpas correspondentes aos cards do App.kt
            val qtdAtualNoEstoque = when (material) {
                "Galho Antigo" -> estoque.galhoAntigo
                "Partículas de Luz" -> estoque.particulaLuz
                "Armadura Dullahan" -> estoque.armaduraDullahan
                "Cabelo Azul" -> estoque.cabeloAzul
                "Garra de Lobo" -> estoque.garraLobo
                "Ouro" -> estoque.ouro // Sincronizado string limpa aqui
                "Dente de Ogre" -> estoque.denteOgre
                "Pergaminho Antigo" -> estoque.pergaminhoAntigo
                "Armadura Destruída" -> estoque.armaduraDestruida
                "Canino de Dragão" -> estoque.caninoDragao
                "Corrente" -> estoque.corrente
                else -> 0
            }

            if (qtdAtualNoEstoque < qtdTotalNecessaria) {
                val unidadesFaltantes = qtdTotalNecessaria - qtdAtualNoEstoque
                faltantesListaData.add(material to unidadesFaltantes)

                val precoUnitario = estoque.precosMercado[material] ?: 0
                custoAcumuladoZeny += (unidadesFaltantes.toLong() * precoUnitario)
            }
        }

        return ResultadoCalculoCraft(faltantesListaData, custoAcumuladoZeny)
    }
}