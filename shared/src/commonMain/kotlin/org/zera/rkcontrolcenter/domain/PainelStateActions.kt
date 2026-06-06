package org.zera.rkcontrolcenter.domain

import org.zera.rkcontrolcenter.AtividadeFarm
import org.zera.rkcontrolcenter.PainelData
import org.zera.rkcontrolcenter.Personagem
import org.zera.rkcontrolcenter.RunasEstoque

fun PainelData.updatePerfilAtivo(transform: (Personagem) -> Personagem): PainelData {
    val perfilAtual = perfis[perfilAtivo] ?: Personagem(nome = perfilAtivo)
    val perfisAtualizados = perfis.toMutableMap().apply {
        put(perfilAtivo, transform(perfilAtual))
    }
    return copy(perfis = perfisAtualizados)
}

fun PainelData.updateEstoqueAtivo(transform: (RunasEstoque) -> RunasEstoque): PainelData =
    updatePerfilAtivo { perfil -> perfil.copy(estoque = transform(perfil.estoque)) }

fun PainelData.updatePrecoMercadoAtivo(item: String, preco: Int): PainelData =
    updateEstoqueAtivo { estoque ->
        val precosAtualizados = estoque.precosMercado.toMutableMap().apply {
            if (preco > 0) {
                put(item, preco)
            } else {
                remove(item)
            }
        }
        estoque.copy(precosMercado = precosAtualizados)
    }

fun PainelData.updateCheckDiarioAtivo(id: String, checked: Boolean): PainelData =
    updatePerfilAtivo { perfil ->
        val checksAtualizados = perfil.checksDiarios.toMutableMap().apply {
            put(id, checked)
        }
        perfil.copy(checksDiarios = checksAtualizados)
    }

fun PainelData.resetChecksAtivo(): PainelData =
    updatePerfilAtivo { perfil -> perfil.copy(checksDiarios = emptyMap()) }

fun PainelData.addAtividadeAtiva(atividade: AtividadeFarm): PainelData =
    updatePerfilAtivo { perfil ->
        perfil.copy(atividadesCustom = perfil.atividadesCustom + atividade)
    }

fun PainelData.removeAtividadeAtiva(id: String): PainelData =
    updatePerfilAtivo { perfil ->
        perfil.copy(
            atividadesCustom = perfil.atividadesCustom.filterNot { it.id == id },
            checksDiarios = perfil.checksDiarios - id
        )
    }

fun PainelData.addPerfil(nome: String): PainelData {
    if (nome.isBlank() || perfis.containsKey(nome)) return this
    val perfisAtualizados = perfis.toMutableMap().apply {
        put(nome, Personagem(nome = nome))
    }
    return copy(perfilAtivo = nome, perfis = perfisAtualizados)
}

fun PainelData.removePerfilAtivo(): PainelData {
    if (perfis.size <= 1) return this

    val perfilRemovido = perfilAtivo
    val perfisAtualizados = perfis.toMutableMap().apply {
        remove(perfilRemovido)
    }
    val novoPerfilAtivo = perfisAtualizados.keys.first()

    return copy(perfilAtivo = novoPerfilAtivo, perfis = perfisAtualizados)
}
