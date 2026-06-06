package org.zera.rkcontrolcenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.zera.rkcontrolcenter.ui.components.*
import org.zera.rkcontrolcenter.ui.theme.*
import rkcontrolcenter.shared.generated.resources.*
import kotlin.time.Clock

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
        // --- ESTADOS GLOBAIS DA APLICAÇÃO ---
        var painelState by remember { mutableStateOf(StorageManager.carregar()) }
        var zenyInputText by remember { mutableStateOf("") }
        val quantidadeDesejadaMap = remember { mutableStateMapOf<String, String>() }
        var mostrarDialogAddCronograma by remember { mutableStateOf(false) }
        var mostrarDialogPix by remember { mutableStateOf(false) } // 🌟 Estado do Popup do QR Code

        val perfilAtual = painelState.perfis[painelState.perfilAtivo] ?: Personagem(nome = "Principal")
        val estoque = perfilAtual.estoque
        val colunasDesejadas = 4

        // --- MAPA DE CORES DAS RUNAS ---
        val coresRunas = remember {
            mapOf(
                "Berkana" to ColorBerkana,
                "Thurisaz" to ColorThurisaz,
                "Luxanima" to ColorLuxanima,
                "Othila" to ColorOthila,
                "Nauthiz" to ColorNauthiz,
                "Wyrd" to ColorWyrd
            )
        }

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

                // 1. SELETOR DE PERFIS
                SeletorPerfis(painelState = painelState) { novoEstado ->
                    StorageManager.salvar(novoEstado)
                    painelState = novoEstado
                }

                // 2. CARD DE ZENY (MODULARIZADO)
                WalletZenyCard(
                    zenyAtual = perfilAtual.zeny,
                    zenyInputText = zenyInputText,
                    onInputChange = { zenyInputText = it },
                    onSalvarClick = {
                        val novoZeny = zenyInputText.toLongOrNull()
                        if (novoZeny != null) {
                            perfilAtual.zeny = novoZeny
                            painelState.perfis[painelState.perfilAtivo] = perfilAtual
                            painelState = painelState.copy()
                            StorageManager.salvar(painelState)
                            zenyInputText = ""
                        }
                    }
                )

                // 3. INGREDIENTES BASE
                Text(
                    text = "INGREDIENTES BASE (TODAS AS RUNAS)",
                    color = White,
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

                // 4. COMPONENTES E MATERIAIS DE FARM
                Text(
                    text = "COMPONENTES E MATERIAIS DE FARM",
                    color = White,
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
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = IconeInstanciaNativo,
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "RUNAS PRINCIPAIS (ESTOQUE)",
                        color = White,
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
                                precoBazar = null,
                                onPrecoMudou = null,
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

                // 6. CALCULADOR DE RUNAS RESPONSIVO (MODULARIZADO)
                Spacer(modifier = Modifier.height(24.dp))
                RunePlannerSection(
                    perfil = perfilAtual,
                    quantidadeDesejadaMap = quantidadeDesejadaMap,
                    coresRunas = coresRunas
                )

                // 7. CRONOGRAMA & ROTAS HEADER
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = IconeMapaNativo,
                            contentDescription = null,
                            tint = ColorZeny,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cronograma & Rotas", color = ColorZeny, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { mostrarDialogAddCronograma = true },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(IconeAdicionarNativo, contentDescription = null, modifier = Modifier.size(16.dp), tint = ColorBackground)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Adicionar", color = ColorBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                // 8. CARDS DE ATIVIDADES DE FARM
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    maxItemsInEachRow = 2
                ) {
                    perfilAtual.atividadesCustom.forEach { atividade ->
                        val isChecked = perfilAtual.checksDiarios[atividade.id] ?: false

                        TarefaFarmRow(
                            modifier = Modifier.weight(1f),
                            name = atividade.name,
                            rota = atividade.rota,
                            loot = atividade.loot,
                            isTest = atividade.test,
                            monstro = atividade.monstro,
                            isChecked = isChecked,
                            onCheckedChange = { chk ->
                                val novoMapa = perfilAtual.checksDiarios.toMutableMap().apply { put(atividade.id, chk) }
                                val n = painelState.copy(perfis = painelState.perfis.toMutableMap().apply {
                                    put(painelState.perfilAtivo, perfilAtual.copy(checksDiarios = novoMapa))
                                })
                                StorageManager.salvar(n); painelState = n
                            },
                            onDeleteClick = {
                                val listaFiltrada = perfilAtual.atividadesCustom.filter { it.id != atividade.id }
                                val checksLimpos = perfilAtual.checksDiarios.toMutableMap().apply { remove(atividade.id) }

                                val n = painelState.copy(perfis = painelState.perfis.toMutableMap().apply {
                                    put(painelState.perfilAtivo, perfilAtual.copy(
                                        atividadesCustom = listaFiltrada,
                                        checksDiarios = checksLimpos
                                    ))
                                })
                                StorageManager.salvar(n)
                                painelState = n
                            }
                        )
                    }
                }

                // 9. BOTÃO DE RESET DIÁRIO
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val n = painelState.copy(
                            perfis = painelState.perfis.toMutableMap().apply {
                                put(painelState.perfilAtivo, perfilAtual.copy(checksDiarios = emptyMap()))
                            }
                        )
                        StorageManager.salvar(n)
                        painelState = n
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF75A5B)),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .height(44.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = IconeResetNativo,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Reset Diário (Limpar Checks)",
                            color = White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
                FooterComponent {
                    mostrarDialogPix = true
                }
            }
        }

        // 10. MODAL DIALOG ADICIONAR ATIVIDADE (MODULARIZADO)
        if (mostrarDialogAddCronograma) {
            DialogAddCronograma(
                onDismiss = { mostrarDialogAddCronograma = false },
                onConfirm = { nome, tipo, rota, loot, monstro, teste ->
                    val novaAtiv = AtividadeFarm(
                        id = "id_${Clock.System.now().toEpochMilliseconds()}",
                        name = nome,
                        rota = rota,
                        loot = loot,
                        test = teste,
                        monstro = monstro
                    )
                    val novaListaAtividades = perfilAtual.atividadesCustom + novaAtiv
                    val n = painelState.copy(
                        perfis = painelState.perfis.toMutableMap().apply {
                            put(painelState.perfilAtivo, perfilAtual.copy(atividadesCustom = novaListaAtividades))
                        }
                    )
                    StorageManager.salvar(n)
                    painelState = n
                    mostrarDialogAddCronograma = false
                }
            )
        }

        // 11. DIALOG INTERATIVO DO QR CODE DO PIX
        if (mostrarDialogPix) {
            DialogPixQrCode(onDismiss = { mostrarDialogPix = false })
        }
    }
}