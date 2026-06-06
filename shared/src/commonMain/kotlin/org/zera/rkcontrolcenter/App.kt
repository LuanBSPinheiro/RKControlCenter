package org.zera.rkcontrolcenter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import rkcontrolcenter.shared.generated.resources.*

// Cores Temáticas do Painel (Material 3)
val ColorBackground = Color(0xFF121214)
val ColorCardBg = Color(0xFF202024)
val ColorPrimary = Color(0xFF00e799)
val ColorZeny = Color(0xFFFFD700)
val ColorBase = Color(0xFFB48EAD)
val ColorBerkana = Color(0xFF52A8FF)
val ColorThurisaz = Color(0xFFA3BE8C)
val ColorLuxanima = Color(0xFFB48EAD)
val ColorOthila = Color(0xFFEBCB8B)
val ColorNauthiz = Color(0xFFD08770)
val ColorWyrd = Color(0xFFB48EAD)
val ColorMaterialComum = Color(0xFF81A1C1)
val ColorMeta = Color(0xFF7C7C8A)

val TarefasDiariasPadrao = listOf(
    "Fazer Instância Diária",
    "Coletar Drops / Quests de Runa",
    "Gasto de Stamina (Mi Gao / Pinguicula)",
    "Rift / Diárias da Guilda"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = ColorPrimary,
            background = ColorBackground,
            surface = ColorCardBg
        )
    ) {
        var painelState by remember { mutableStateOf(StorageManager.carregar()) }
        var zenyInputText by remember { mutableStateOf("") }
        val quantidadeDesejadaMap = remember { mutableStateMapOf<String, String>() }

        val perfilAtual = painelState.perfis[painelState.perfilAtivo] ?: Personagem(nome = "Principal")
        val estoque = perfilAtual.estoque

        // Mapeamento das receitas passadas por você
        val receitasRunas = mapOf(
            "Berkana" to listOf("Galho Antigo" to 1, "Armadura Dullahan" to 1),
            "Thurisaz" to listOf("Galho Antigo" to 1, "Cabelo Azul" to 1, "Garra de Lobo do Deserto" to 1),
            "Luxanima" to listOf("Galho Antigo" to 1, "Partícula de Luz" to 3, "Ouro" to 3),
            "Othila" to listOf("Partícula de Luz" to 1, "Dente de Ogre" to 1, "Galho Antigo" to 1),
            "Nauthiz" to listOf(
                "Pergaminho Antigo" to 1,
                "Armadura Destruída" to 1,
                "Partícula de Luz" to 1,
                "Galho Antigo" to 1
            ),
            "Wyrd" to listOf("Galho Antigo" to 1, "Partícula de Luz" to 1, "Canino de Dragão" to 1, "Corrente" to 1)
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("RK Control Center - LATAM", color = ColorPrimary, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorCardBg)
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorBackground)
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // === CARD DE ZENY ===
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorCardBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("CARTEIRA DE ZENY", color = ColorZeny, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                        val zenyFormatado =
                            perfilAtual.zeny.toString().reversed().chunked(3).joinToString(".").reversed()
                        Text(
                            "$zenyFormatado z",
                            color = ColorZeny,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = zenyInputText,
                                onValueChange = { zenyInputText = it },
                                label = { Text("Atualizar Zeny total") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ColorPrimary,
                                    unfocusedBorderColor = Color.Gray,
                                    focusedLabelColor = ColorPrimary,
                                    unfocusedLabelColor = Color.Gray
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    val novoZeny = zenyInputText.toLongOrNull()
                                    if (novoZeny != null) {
                                        perfilAtual.zeny = novoZeny
                                        painelState.perfis[painelState.perfilAtivo] = perfilAtual
                                        painelState = painelState.copy()
                                        StorageManager.salvar(painelState)
                                        zenyInputText = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
                                modifier = Modifier.height(56.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Salvar", color = ColorBackground, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // === INGREDIENTES BASE ===
                Text(
                    "INGREDIENTES BASE (TODAS AS RUNAS)",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    ItemEstoqueCard(
                        modifier = Modifier.weight(1f),
                        titulo = "Galho Antigo",
                        quantidade = estoque.galhoAntigo,
                        meta = "Meta: 8-10",
                        corTitulo = ColorBase,
                        icone = Res.drawable.galho_antigo
                    ) { novaQtd ->
                        val novoEstoque = estoque.copy(galhoAntigo = novaQtd)
                        val novoPersonagem = perfilAtual.copy(estoque = novoEstoque)
                        val novosPerfis =
                            painelState.perfis.toMutableMap().apply { put(painelState.perfilAtivo, novoPersonagem) }
                        val novoEstado = painelState.copy(perfis = novosPerfis)
                        StorageManager.salvar(novoEstado)
                        painelState = novoEstado
                    }

                    ItemEstoqueCard(
                        modifier = Modifier.weight(1f),
                        titulo = "Particula de Luz",
                        quantidade = estoque.particulaLuz,
                        meta = "Meta: 8-10",
                        corTitulo = ColorLuxanima,
                        icone = Res.drawable.particulas_luz
                    ) { novaQtd ->
                        val novoEstoque = estoque.copy(particulaLuz = novaQtd)
                        val novoPersonagem = perfilAtual.copy(estoque = novoEstoque)
                        val novosPerfis =
                            painelState.perfis.toMutableMap().apply { put(painelState.perfilAtivo, novoPersonagem) }
                        val novoEstado = painelState.copy(perfis = novosPerfis)
                        StorageManager.salvar(novoEstado)
                        painelState = novoEstado
                    }
                }

// === COMPONENTES ESPECÍFICOS E MATERIAIS ===
                Text(
                    "COMPONENTES E MATERIAIS DE FARM",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp)
                )

// 📐 DEFINIÇÃO DO GRID: Mude o valor abaixo para 3 ou 4 conforme preferir o visual!
                val colunasDesejadas = 4

                FlowRow(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    maxItemsInEachRow = colunasDesejadas
                ) {
                    // 1. Armadura Dullahan
                    Box(modifier = Modifier.weight(1f)) {
                        ItemEstoqueCard(
                            titulo = "Armadura Dullahan",
                            quantidade = estoque.armaduraDullahan,
                            meta = "Meta: 15-20",
                            corTitulo = ColorBerkana,
                            icone = Res.drawable.armadura_dullahan
                        ) { novaQtd ->
                            val novoEstado = painelState.copy(
                                perfis = painelState.perfis.toMutableMap().apply {
                                    put(
                                        painelState.perfilAtivo,
                                        perfilAtual.copy(estoque = estoque.copy(armaduraDullahan = novaQtd))
                                    )
                                })
                            StorageManager.salvar(novoEstado); painelState = novoEstado
                        }
                    }

                    // 2. Cabelo Azul
                    Box(modifier = Modifier.weight(1f)) {
                        ItemEstoqueCard(
                            titulo = "Cabelo Azul",
                            quantidade = estoque.cabeloAzul,
                            meta = "Meta: 15-20",
                            corTitulo = ColorThurisaz,
                            icone = Res.drawable.cabelo_azul
                        ) { novaQtd ->
                            val novoEstado = painelState.copy(
                                perfis = painelState.perfis.toMutableMap().apply {
                                    put(
                                        painelState.perfilAtivo,
                                        perfilAtual.copy(estoque = estoque.copy(cabeloAzul = novaQtd))
                                    )
                                })
                            StorageManager.salvar(novoEstado); painelState = novoEstado
                        }
                    }

                    // 3. Garra de Lobo
                    Box(modifier = Modifier.weight(1f)) {
                        ItemEstoqueCard(
                            titulo = "Garra de Lobo do Deserto",
                            quantidade = estoque.garraLobo,
                            meta = "Meta: 15-20",
                            corTitulo = ColorThurisaz,
                            icone = Res.drawable.garra_lobo_deserto
                        ) { novaQtd ->
                            val novoEstado = painelState.copy(
                                perfis = painelState.perfis.toMutableMap().apply {
                                    put(
                                        painelState.perfilAtivo,
                                        perfilAtual.copy(estoque = estoque.copy(garraLobo = novaQtd))
                                    )
                                })
                            StorageManager.salvar(novoEstado); painelState = novoEstado
                        }
                    }

                    // 4. Ouro (Mi Gao)
                    Box(modifier = Modifier.weight(1f)) {
                        ItemEstoqueCard(
                            titulo = "Ouro (Mi Gao)",
                            quantidade = estoque.ouro,
                            meta = "Meta: 1-2",
                            corTitulo = ColorZeny,
                            icone = Res.drawable.ouro
                        ) { novaQtd ->
                            val novoEstado = painelState.copy(
                                perfis = painelState.perfis.toMutableMap().apply {
                                    put(
                                        painelState.perfilAtivo,
                                        perfilAtual.copy(estoque = estoque.copy(ouro = novaQtd))
                                    )
                                })
                            StorageManager.salvar(novoEstado); painelState = novoEstado
                        }
                    }

                    // 5. Dente de Ogre
                    Box(modifier = Modifier.weight(1f)) {
                        ItemEstoqueCard(
                            titulo = "Dente de Ogre",
                            quantidade = estoque.denteOgre,
                            meta = "Meta: 10",
                            corTitulo = ColorOthila,
                            icone = Res.drawable.dente_ogre
                        ) { novaQtd ->
                            val novoEstado = painelState.copy(
                                perfis = painelState.perfis.toMutableMap().apply {
                                    put(
                                        painelState.perfilAtivo,
                                        perfilAtual.copy(estoque = estoque.copy(denteOgre = novaQtd))
                                    )
                                })
                            StorageManager.salvar(novoEstado); painelState = novoEstado
                        }
                    }

                    // 6. Pergaminho Antigo
                    Box(modifier = Modifier.weight(1f)) {
                        ItemEstoqueCard(
                            titulo = "Pergaminho Antigo",
                            quantidade = estoque.pergaminhoAntigo,
                            meta = "Meta: 10-20",
                            corTitulo = ColorMaterialComum,
                            icone = Res.drawable.pergaminho_antigo
                        ) { novaQtd ->
                            val novoEstado = painelState.copy(
                                perfis = painelState.perfis.toMutableMap().apply {
                                    put(
                                        painelState.perfilAtivo,
                                        perfilAtual.copy(estoque = estoque.copy(pergaminhoAntigo = novaQtd))
                                    )
                                })
                            StorageManager.salvar(novoEstado); painelState = novoEstado
                        }
                    }

                    // 7. Armadura Destruída
                    Box(modifier = Modifier.weight(1f)) {
                        ItemEstoqueCard(
                            titulo = "Armadura Destruída",
                            quantidade = estoque.armaduraDestruida,
                            meta = "Meta: 20",
                            corTitulo = ColorMaterialComum,
                            icone = Res.drawable.armadura_destruida
                        ) { novaQtd ->
                            val novoEstado = painelState.copy(
                                perfis = painelState.perfis.toMutableMap().apply {
                                    put(
                                        painelState.perfilAtivo,
                                        perfilAtual.copy(estoque = estoque.copy(armaduraDestruida = novaQtd))
                                    )
                                })
                            StorageManager.salvar(novoEstado); painelState = novoEstado
                        }
                    }

                    // 8. Canino de Dragão
                    Box(modifier = Modifier.weight(1f)) {
                        ItemEstoqueCard(
                            titulo = "Canino de Dragão",
                            quantidade = estoque.caninoDragao,
                            meta = "Meta: 20-30",
                            corTitulo = ColorMaterialComum,
                            icone = Res.drawable.canino_dragao
                        ) { novaQtd ->
                            val novoEstado = painelState.copy(
                                perfis = painelState.perfis.toMutableMap().apply {
                                    put(
                                        painelState.perfilAtivo,
                                        perfilAtual.copy(estoque = estoque.copy(caninoDragao = novaQtd))
                                    )
                                })
                            StorageManager.salvar(novoEstado); painelState = novoEstado
                        }
                    }

                    // 9. Corrente (O último item automaticamente vai expandir e ocupar o peso restante na linha!)
                    Box(modifier = Modifier.weight(1f)) {
                        ItemEstoqueCard(
                            titulo = "Corrente",
                            quantidade = estoque.corrente,
                            meta = "Meta: 30",
                            corTitulo = ColorMaterialComum,
                            icone = Res.drawable.correntes
                        ) { novaQtd ->
                            val novoEstado = painelState.copy(
                                perfis = painelState.perfis.toMutableMap().apply {
                                    put(
                                        painelState.perfilAtivo,
                                        perfilAtual.copy(estoque = estoque.copy(corrente = novaQtd))
                                    )
                                })
                            StorageManager.salvar(novoEstado); painelState = novoEstado
                        }
                    }
                }

                // === RUNAS PRINCIPAIS (CONCLUÍDAS) ===
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "🛡️ RUNAS PRINCIPAIS (ESTOQUE)",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                FlowRow(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    maxItemsInEachRow = colunasDesejadas // Mantém o mesmo padrão visual (3 ou 4 colunas)
                ) {
                    val listaRunas = listOf(
                        Triple("Berkana", estoque.runaBerkana, ColorBerkana to Res.drawable.berkana),
                        Triple("Thurisaz", estoque.runaThurisaz, ColorThurisaz to Res.drawable.thurisaz),
                        Triple("Luxanima", estoque.runaLuxanima, ColorLuxanima to Res.drawable.luxanima),
                        Triple("Othila", estoque.runaOthila, ColorOthila to Res.drawable.othila),
                        Triple("Nauthiz", estoque.runaNauthiz, ColorNauthiz to Res.drawable.nauthiz),
                        Triple("Wyrd", estoque.runaWyrd, ColorWyrd to Res.drawable.wyrd)
                    )

                    listaRunas.forEach { (nome, qtd, estilo) ->
                        Box(modifier = Modifier.weight(1f)) {
                            ItemEstoqueCard(
                                titulo = "Runa $nome",
                                quantidade = qtd,
                                meta = "Disponível",
                                corTitulo = estilo.first,
                                icone = estilo.second
                            ) { novaQtd ->
                                val novoEstoque = when (nome) {
                                    "Berkana" -> estoque.copy(runaBerkana = novaQtd)
                                    "Thurisaz" -> estoque.copy(runaThurisaz = novaQtd)
                                    "Luxanima" -> estoque.copy(runaLuxanima = novaQtd)
                                    "Othila" -> estoque.copy(runaOthila = novaQtd)
                                    "Nauthiz" -> estoque.copy(runaNauthiz = novaQtd)
                                    else -> estoque.copy(runaWyrd = novaQtd)
                                }
                                val novoEstado = painelState.copy(perfis = painelState.perfis.toMutableMap().apply {
                                    put(painelState.perfilAtivo, perfilAtual.copy(estoque = novoEstoque))
                                })
                                StorageManager.salvar(novoEstado); painelState = novoEstado
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // === CALCULADOR DE RUNAS / MATRIZ DE CRAFT ===
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "🔮 CALCULADOR DE RUNAS (PLANEJAMENTO DE CRAFT)",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                val coresRunas = mapOf(
                    "Berkana" to ColorBerkana, "Thurisaz" to ColorThurisaz, "Luxanima" to ColorLuxanima,
                    "Othila" to ColorOthila, "Nauthiz" to ColorNauthiz, "Wyrd" to ColorWyrd
                )

                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val larguraCard = (maxWidth - (12.dp * (colunasDesejadas - 1))) / colunasDesejadas

                    FlowRow(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        maxItemsInEachRow = colunasDesejadas
                    ) {
                        receitasRunas.forEach { (nomeRuna, receita) ->
                            // Recupera o texto do input ou assume vazio se for a primeira vez
                            val inputText = quantidadeDesejadaMap[nomeRuna] ?: ""
                            // Converte para Int estável (se estiver vazio, assume meta de 1 runa padrão)
                            val qtdDesejada = inputText.toIntOrNull() ?: 1

                            val faltantes = calcularFaltantes(estoque, receita, qtdDesejada)
                            val podeCraftar = faltantes.isEmpty() && qtdDesejada > 0
                            val corTema = coresRunas[nomeRuna] ?: Color.White

                            Card(
                                modifier = Modifier
                                    .width(larguraCard)
                                    .heightIn(min = 180.dp), // Aumentamos um pouco a altura mínima para acomodar o input
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = ColorCardBg),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    // Linha de Cabeçalho com Nome e Input Lado a Lado
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Craft $nomeRuna",
                                            color = corTema,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.weight(1f)
                                        )

                                        // Mini Input de Quantidade Alvo
                                        OutlinedTextField(
                                            value = inputText,
                                            onValueChange = { newValue: String ->
                                                if (newValue.all { it.isDigit() }) {
                                                    quantidadeDesejadaMap[nomeRuna] = newValue
                                                }
                                            },
                                            placeholder = {
                                                Text(text = "1", fontSize = 11.sp, color = ColorMeta)
                                            },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            modifier = Modifier.width(55.dp).height(48.dp),
                                            textStyle = androidx.compose.ui.text.TextStyle(
                                                fontSize = 12.sp,
                                                color = Color.White,
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                            ),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = corTema,
                                                unfocusedBorderColor = Color.DarkGray,
                                                focusedContainerColor = ColorBackground,
                                                unfocusedContainerColor = ColorBackground
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Renderização do Diagnóstico de Farm
                                    if (podeCraftar) {
                                        Text(
                                            text = "🟢 Pronto para criar $qtdDesejada!",
                                            color = ColorPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    } else if (qtdDesejada <= 0) {
                                        Text(text = "⚪ Insira uma meta válida", color = ColorMeta, fontSize = 12.sp)
                                    } else {
                                        Column {
                                            Text(
                                                text = "🔴 Falta para $qtdDesejada runa(s):",
                                                color = Color.Red,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            faltantes.forEach { item ->
                                                Text(
                                                    text = "• $item",
                                                    color = Color.LightGray,
                                                    fontSize = 12.sp,
                                                    modifier = Modifier.padding(start = 6.dp, bottom = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // === CHECKLIST DIÁRIO (AGORA NO ESCOPO VERTICAL DA COLUMN) ===
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "✓ TAREFAS DIÁRIAS (RESET DIÁRIO)",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorCardBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // Passa por cada tarefa padrão e monta a linha com o checkbox
                        TarefasDiariasPadrao.forEach { tarefa ->
                            val isChecked = perfilAtual.checksDiarios[tarefa] ?: false

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        val novosChecks = perfilAtual.checksDiarios.toMutableMap().apply {
                                            put(tarefa, checked)
                                        }

                                        val novoPersonagem = perfilAtual.copy(checksDiarios = novosChecks)

                                        val novosPerfis = painelState.perfis.toMutableMap().apply {
                                            put(painelState.perfilAtivo, novoPersonagem)
                                        }

                                        val novoEstado = painelState.copy(perfis = novosPerfis)
                                        StorageManager.salvar(novoEstado)
                                        painelState = novoEstado
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = ColorPrimary,
                                        uncheckedColor = Color.Gray,
                                        checkmarkColor = ColorBackground
                                    )
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = tarefa,
                                    color = if (isChecked) ColorMeta else Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = if (isChecked) FontWeight.Normal else FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            if (tarefa != TarefasDiariasPadrao.last()) {
                                HorizontalDivider(
                                    color = Color.DarkGray,
                                    thickness = 0.5.dp,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                        }
                    }
                }
            } // Fecha o escopo da Column principal
        } // Fecha o Scaffold
    } // Fecha o MaterialTheme
} // Fecha a fun App()

// === COMPONENTE COMPARTILHADO REUTILIZÁVEL PARA OS CARDS DE ITENS ===
@Composable
fun ItemEstoqueCard(
    modifier: Modifier = Modifier,
    titulo: String,
    quantidade: Int,
    meta: String,
    corTitulo: Color,
    icone: DrawableResource, // 👈 Voltamos a receber o recurso nativo tipado
    onQuantidadeMudou: (Int) -> Unit
) {
    var textInput by remember(quantidade) { mutableStateOf("") }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = ColorCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Renderização local nativa direto no Canvas (A prova de CORS e falhas de rede)
            Image(
                painter = painterResource(icone),
                contentDescription = titulo,
                modifier = Modifier
                    .size(56.dp)
                    .padding(bottom = 6.dp)
            )

            Text(text = titulo, color = corTitulo, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(
                text = quantidade.toString(),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(text = meta, color = ColorMeta, fontSize = 11.sp, modifier = Modifier.padding(bottom = 8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Qtd", fontSize = 11.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(65.dp).height(48.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorPrimary,
                        unfocusedBorderColor = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Button(
                    onClick = {
                        val novaQtd = textInput.toIntOrNull()
                        if (novaQtd != null) {
                            onQuantidadeMudou(novaQtd)
                            textInput = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(36.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("✓", color = ColorBackground, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Função utilitária atualizada para calcular o farm multiplicando pela meta desejada
fun calcularFaltantes(
    estoque: RunasEstoque,
    receita: List<Pair<String, Int>>,
    quantidadeDesejada: Int // 👈 Novo parâmetro multiplicador
): List<String> {
    val faltantes = mutableListOf<String>()
    if (quantidadeDesejada <= 0) return faltantes // Se não quer fazer nenhuma, não falta nada

    receita.forEach { (material, qtdPorRuna) ->
        // Multiplica o custo unitário da receita pela quantidade de runas que o usuário quer craftar
        val qtdTotalNecessaria = qtdPorRuna * quantidadeDesejada

        val qtdAtualNoEstoque = when (material) {
            "Galho Antigo" -> estoque.galhoAntigo
            "Partícula de Luz" -> estoque.particulaLuz
            "Armadura Dullahan" -> estoque.armaduraDullahan
            "Cabelo Azul" -> estoque.cabeloAzul
            "Garra de Lobo do Deserto" -> estoque.garraLobo
            "Ouro" -> estoque.ouro
            "Dente de Ogre" -> estoque.denteOgre
            "Pergaminho Antigo" -> estoque.pergaminhoAntigo
            "Armadura Destruída" -> estoque.armaduraDestruida
            "Canino de Dragão" -> estoque.caninoDragao
            "Corrente" -> estoque.corrente
            else -> 0
        }

        // Verifica se o estoque atual é insuficiente para a meta total de crafts
        if (qtdAtualNoEstoque < qtdTotalNecessaria) {
            val diferenca = qtdTotalNecessaria - qtdAtualNoEstoque
            faltantes.add("$diferenca $material(s)")
        }
    }
    return faltantes
}