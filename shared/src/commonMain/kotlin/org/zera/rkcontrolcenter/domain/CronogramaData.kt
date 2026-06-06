package org.zera.rkcontrolcenter.domain

import org.zera.rkcontrolcenter.AtividadeFarm

// Data class estruturada mantendo o contrato limpo do SOLID

val ListaAtividadesCronograma = listOf(
    AtividadeFarm(
        "sara",
        "Memórias de Sara",
        "Instância",
        "Comidas de Atributo (Guardar para vender para Jogadores ou uso próprio)"
    ),
    AtividadeFarm(
        "palacio",
        "Palácio das Mágoas",
        "Instância",
        "Estilhaços -> Armas de Cinzas para Zeny direto no NPC"
    ),
    AtividadeFarm("orcs", "Memória dos Orcs", "Instância", "Moedas da instância e loots gerais"),
    AtividadeFarm(
        "splendide",
        "Farm: Galho Antigo (Pinguicula)",
        "Mapa: spl_fild02",
        "Rota: Vá para o Acampamento de Esplendor -> Entre 1 mapa à Esquerda"
    ),
    AtividadeFarm(
        "leao_montanha",
        "Farm: Partículas de Luz (Leão da Montanha)",
        "Mapas: man_fild03 ou spl_fild03",
        "Rota: Acampamento -> 1 Direita e 1 Baixo (man_fild03) OU desça direto de spl_fild02"
    ),
    AtividadeFarm(
        "dullahan",
        "Farm: Armadura de Dullahan (Dullahan)",
        "Mapa: nif_fild01",
        "Rota: Campos de Nifflheim (Masmorra dos Mortos-Vivos)"
    ),
    AtividadeFarm("kobold", "Farm: Cabelo Azul (Kobolds)", "Mapa: ra_fild05", "Rota: Campos de Rachel"),
    AtividadeFarm("lobo_deserto", "Farm: Garra de Lobo (Lobo do Deserto)", "Mapa: ra_fild01", "Rota: Campos de Rachel"),
    AtividadeFarm(
        "high_orcs",
        "Farm: Dente de Ogre (High Orc)",
        "Mapa: gef_fild02",
        "Rota: Campos de Geffen (Vila dos Orcs)"
    ),
    AtividadeFarm("louyang", "Farm: Ouro (Mi Gao)", "Mapa: lou_fild01", "Rota: Arredores de Louyang"),

    // Passamos explicitamente o booleano apenas onde ele muda de estado!
    AtividadeFarm(
        "geffen",
        "[TESTAR] TESTE: Torneio de Geffen",
        "Instância",
        "Testar o Set Grácil +9 contra a Fey Kanavion",
        test = true
    ),
    AtividadeFarm(
        "ogh",
        "[TESTAR] TESTE: Maldição de Glast Heim (OGH)",
        "Instância",
        "Testar sobrevivência contra os Cavaleiros Abissais",
        test = true
    )
)