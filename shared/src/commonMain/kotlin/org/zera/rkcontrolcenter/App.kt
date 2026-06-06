package org.zera.rkcontrolcenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.zera.rkcontrolcenter.domain.MatrizCraft
import org.zera.rkcontrolcenter.ui.components.*
import org.zera.rkcontrolcenter.ui.theme.*
import rkcontrolcenter.shared.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
        val colunasDesejadas = 4

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("RK Control Center - LATAM", color = ColorPrimary, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorCardBg)
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().background(ColorBackground).padding(paddingValues).padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 1. SELETOR DE PERFIS (MODULARIZADO)
                SeletorPerfis(painelState = painelState) { novoEstado ->
                    StorageManager.salvar(novoEstado)
                    painelState = novoEstado
                }

                // 2. CARD DE ZENY
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorCardBg)
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
                                modifier = Modifier.weight(1f)
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
                                modifier = Modifier.height(56.dp)
                            ) {
                                Text("Salvar", color = ColorBackground, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // 3. INGREDIENTES BASE
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
                        icone = Res.drawable.galho_antigo,
                        precoBazar = estoque.precosMercado["Galho Antigo"] ?: 0,
                        onQuantidadeMudou = { q ->
                            val n = painelState.copy(perfis = painelState.perfis.toMutableMap().apply { put(painelState.perfilAtivo, perfilAtual.copy(estoque = estoque.copy(galhoAntigo = q))) })
                            StorageManager.salvar(n); painelState = n
                        },
                        onPrecoMudou = { p ->
                            val novosPrecos = estoque.precosMercado.toMutableMap().apply { put("Galho Antigo", p) }
                            val n = painelState.copy(perfis = painelState.perfis.toMutableMap().apply { put(painelState.perfilAtivo, perfilAtual.copy(estoque = estoque.copy(precosMercado = novosPrecos))) })
                            StorageManager.salvar(n); painelState = n
                        }
                    )

                    ItemEstoqueCard(
                        modifier = Modifier.weight(1f),
                        titulo = "Partículas de Luz",
                        quantidade = estoque.particulaLuz,
                        meta = "Meta: 8-10",
                        corTitulo = ColorLuxanima,
                        icone = Res.drawable.particulas_luz,
                        precoBazar = estoque.precosMercado["Partículas de Luz"] ?: 0,
                        onQuantidadeMudou = { q ->
                            val n = painelState.copy(perfis = painelState.perfis.toMutableMap().apply { put(painelState.perfilAtivo, perfilAtual.copy(estoque = estoque.copy(galhoAntigo = q))) })
                            StorageManager.salvar(n); painelState = n
                        },
                        onPrecoMudou = { p ->
                            val novosPrecos = estoque.precosMercado.toMutableMap().apply { put("Partículas de Luz", p) }
                            val n = painelState.copy(perfis = painelState.perfis.toMutableMap().apply { put(painelState.perfilAtivo, perfilAtual.copy(estoque = estoque.copy(precosMercado = novosPrecos))) })
                            StorageManager.salvar(n); painelState = n
                        }
                    )
                }

                // 4. COMPONENTES E MATERIAIS DE FARM (GRID FLUIDO 4 COLUNAS CORRIGIDO)
                Text(
                    "COMPONENTES E MATERIAIS DE FARM",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp)
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    maxItemsInEachRow = colunasDesejadas
                ) {
                    val m = listOf(
                        Triple("Armadura Dullahan", estoque.armaduraDullahan, ColorBerkana to Res.drawable.armadura_dullahan),
                        Triple("Cabelo Azul", estoque.cabeloAzul, ColorThurisaz to Res.drawable.cabelo_azul),
                        Triple("Garra de Lobo", estoque.garraLobo, ColorThurisaz to Res.drawable.garra_lobo_deserto),
                        Triple("Ouro", estoque.ouro, ColorZeny to Res.drawable.ouro),
                        Triple("Dente de Ogre", estoque.denteOgre, ColorOthila to Res.drawable.dente_ogre),
                        Triple("Pergaminho Antigo", estoque.pergaminhoAntigo, ColorMaterialComum to Res.drawable.pergaminho_antigo),
                        Triple("Armadura Destruída", estoque.armaduraDestruida, ColorMaterialComum to Res.drawable.armadura_destruida),
                        Triple("Canino de Dragão", estoque.caninoDragao, ColorMaterialComum to Res.drawable.canino_dragao),
                        Triple("Corrente", estoque.corrente, ColorMaterialComum to Res.drawable.correntes)
                    )

                    m.forEach { (nome, qtd, estilo) ->
                        Box(modifier = Modifier.weight(1f)) {
                            ItemEstoqueCard(
                                titulo = nome,
                                quantidade = qtd,
                                meta = "Farm",
                                corTitulo = estilo.first,
                                icone = estilo.second,
                                // ➕ PLUGANDO AS PROPRIEDADES DE PREÇO DO BAZAR:
                                precoBazar = estoque.precosMercado[nome] ?: 0,
                                onQuantidadeMudou = { q ->
                                    val novoEstoque = when (nome) {
                                        "Armadura Dullahan" -> estoque.copy(armaduraDullahan = q)
                                        "Cabelo Azul" -> estoque.copy(cabeloAzul = q)
                                        "Garra de Lobo" -> estoque.copy(garraLobo = q)
                                        "Ouro" -> estoque.copy(ouro = q)
                                        "Dente de Ogre" -> estoque.copy(denteOgre = q)
                                        "Pergaminho Antigo" -> estoque.copy(pergaminhoAntigo = q)
                                        "Armadura Destruída" -> estoque.copy(armaduraDestruida = q)
                                        "Canino de Dragão" -> estoque.copy(caninoDragao = q)
                                        else -> estoque.copy(corrente = q)
                                    }
                                    val n = painelState.copy(perfis = painelState.perfis.toMutableMap().apply {
                                        put(painelState.perfilAtivo, perfilAtual.copy(estoque = novoEstoque))
                                    })
                                    StorageManager.salvar(n); painelState = n
                                },
                                onPrecoMudou = { p ->
                                    // Atualiza dinamicamente o preço do item atual no Map de precosMercado
                                    val novosPrecos = estoque.precosMercado.toMutableMap().apply { put(nome, p) }
                                    val n = painelState.copy(perfis = painelState.perfis.toMutableMap().apply {
                                        put(painelState.perfilAtivo, perfilAtual.copy(estoque = estoque.copy(precosMercado = novosPrecos)))
                                    })
                                    StorageManager.salvar(n); painelState = n
                                }
                            )
                        }
                    }
                }

                // 5. RUNAS PRINCIPAIS (ESTOQUE)
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
                    maxItemsInEachRow = colunasDesejadas
                ) {
                    val r = listOf(
                        Triple("Berkana", estoque.runaBerkana, ColorBerkana to Res.drawable.berkana),
                        Triple("Thurisaz", estoque.runaThurisaz, ColorThurisaz to Res.drawable.thurisaz),
                        Triple("Luxanima", estoque.runaLuxanima, ColorLuxanima to Res.drawable.luxanima),
                        Triple("Othila", estoque.runaOthila, ColorOthila to Res.drawable.othila),
                        Triple("Nauthiz", estoque.runaNauthiz, ColorNauthiz to Res.drawable.nauthiz),
                        Triple("Wyrd", estoque.runaWyrd, ColorWyrd to Res.drawable.wyrd)
                    )
                    r.forEach { (nome, qtd, estilo) ->
                        Box(modifier = Modifier.weight(1f)) {
                            ItemEstoqueCard(
                                titulo = "Runa $nome",
                                quantidade = qtd,
                                meta = "Pronta",
                                corTitulo = estilo.first,
                                icone = estilo.second,
                                precoBazar = null,   // Runas prontas não têm preço de Bazar
                                onPrecoMudou = null, // Oculta o input amarelo de preço
                                onQuantidadeMudou = { q ->
                                    val novoEstoque = when (nome) {
                                        "Berkana" -> estoque.copy(runaBerkana = q)
                                        "Thurisaz" -> estoque.copy(runaThurisaz = q)
                                        "Luxanima" -> estoque.copy(runaLuxanima = q)
                                        "Othila" -> estoque.copy(runaOthila = q)
                                        "Nauthiz" -> estoque.copy(runaNauthiz = q)
                                        else -> estoque.copy(runaWyrd = q)
                                    }
                                    val n = painelState.copy(
                                        perfis = painelState.perfis.toMutableMap().apply {
                                            put(painelState.perfilAtivo, perfilAtual.copy(estoque = novoEstoque))
                                        })
                                    StorageManager.salvar(n); painelState = n
                                }
                            )
                        }
                    }
                }

                // 6. CALCULADOR DE RUNAS REATIVO
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "🔮 CALCULADOR DE RUNAS (PLANEJAMENTO)",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                val coresRunas = mapOf(
                    "Berkana" to ColorBerkana,
                    "Thurisaz" to ColorThurisaz,
                    "Luxanima" to ColorLuxanima,
                    "Othila" to ColorOthila,
                    "Nauthiz" to ColorNauthiz,
                    "Wyrd" to ColorWyrd
                )

                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val larguraCard = (maxWidth - (12.dp * (colunasDesejadas - 1))) / colunasDesejadas
                    FlowRow(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        maxItemsInEachRow = colunasDesejadas
                    ) {
                        MatrizCraft.receitasRunas.forEach { (nomeRuna, receita) ->
                            val inputText = quantidadeDesejadaMap[nomeRuna] ?: ""
                            val qtdDesejada = inputText.toIntOrNull() ?: 1

                            val result = MatrizCraft.calcularCraft(estoque, receita, qtdDesejada)
                            val podeCraftar = result.faltantesDetalhados.isEmpty() && qtdDesejada > 0
                            val corTema = coresRunas[nomeRuna] ?: Color.White

                            Card(
                                modifier = Modifier.width(larguraCard).heightIn(min = 180.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = ColorCardBg)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Craft $nomeRuna",
                                            color = corTema,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.weight(1f)
                                        )
                                        OutlinedTextField(
                                            value = inputText,
                                            onValueChange = { nv ->
                                                if (nv.all { it.isDigit() }) quantidadeDesejadaMap[nomeRuna] = nv
                                            },
                                            placeholder = { Text("1", fontSize = 11.sp, color = ColorMeta) },
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
                                    if (podeCraftar) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier.size(8.dp)
                                                    .background(ColorPrimary, shape = CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Pronto para criar $qtdDesejada!",
                                                color = ColorPrimary,
                                                fontSize = 13.sp
                                            )
                                        }
                                    } else {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                // Indicador circular nativo vermelho
                                                Box(modifier = Modifier.size(8.dp).background(Color.Red, shape = CircleShape))
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(text = "Falta para $qtdDesejada runa(s):", color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))

                                            // Renderização detalhada dos itens faltantes
                                            result.faltantesDetalhados.forEach { detalhe ->
                                                val material = detalhe.first
                                                val unitsMissing = detalhe.second

                                                // Puxa o preço do Bazar para este material específico
                                                val precoBazar = estoque.precosMercado[material] ?: 0
                                                // Calcula o custo apenas para a quantidade faltante
                                                val itemCost = unitsMissing.toLong() * precoBazar

                                                // Formata o custo individual
                                                val formattedCost = if (itemCost > 0L) {
                                                    " " + itemCost.toString().reversed().chunked(3).joinToString(".").reversed() + " z"
                                                } else ""

                                                // Texto final: "• 20 Ouro(s) 4.800.000 z"
                                                Text(
                                                    text = buildAnnotatedString {
                                                        append("• $unitsMissing $material(s)")
                                                        withStyle(style = androidx.compose.ui.text.SpanStyle(color = ColorZeny, fontWeight = FontWeight.Medium)) {
                                                            append(formattedCost)
                                                        }
                                                    },
                                                    color = Color.LightGray,
                                                    fontSize = 12.sp,
                                                    modifier = Modifier.padding(start = 6.dp, bottom = 2.dp)
                                                )
                                            }

                                            // Exibição do custo total da runa no rodapé
                                            if (result.custoTotalZeny > 0) {
                                                Spacer(modifier = Modifier.height(8.dp))
                                                HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))

                                                val zenyTotalFormatado = result.custoTotalZeny.toString().reversed().chunked(3).joinToString(".").reversed()

                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = IconeMoedaRodapeNativo,
                                                        contentDescription = null,
                                                        tint = ColorZeny,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "Custo Total: $zenyTotalFormatado z",
                                                        color = ColorZeny,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // 7. CHECKLIST DIÁRIO
                Spacer(modifier = Modifier.height(24.dp))
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
                    colors = CardDefaults.cardColors(containerColor = ColorCardBg)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TarefasDiariasPadrao.forEach { tarefa ->
                            val isChecked = perfilAtual.checksDiarios[tarefa] ?: false
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(checked = isChecked, onCheckedChange = { chk ->
                                    val nc = perfilAtual.checksDiarios.toMutableMap().apply { put(tarefa, chk) }
                                    val n = painelState.copy(
                                        perfis = painelState.perfis.toMutableMap().apply {
                                            put(
                                                painelState.perfilAtivo,
                                                perfilAtual.copy(checksDiarios = nc)
                                            )
                                        })
                                    StorageManager.salvar(n); painelState = n
                                })
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = tarefa,
                                    color = if (isChecked) ColorMeta else Color.White,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (tarefa != TarefasDiariasPadrao.last()) HorizontalDivider(
                                color = Color.DarkGray,
                                thickness = 0.5.dp
                            )
                        }
                    }
                }
            }
        }
    }
}