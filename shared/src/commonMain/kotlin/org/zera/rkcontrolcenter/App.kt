package org.zera.rkcontrolcenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Cores Temáticas do Painel (Material 3)
val ColorBackground = Color(0xFF121214)
val ColorCardBg = Color(0xFF202024)
val ColorPrimary = Color(0xFF00e799)
val ColorZeny = Color(0xFFFFD700)
val ColorBase = Color(0xFFB48EAD)
val ColorBerkana = Color(0xFF52A8FF)
val ColorThurisaz = Color(0xFFA3BE8C)
val ColorLuxanima = Color(0xFFB48EAD)
val ColorOthila = Color(0xEBCB8B)
val ColorMeta = Color(0xFF7C7C8A)

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

        val perfilAtual = painelState.perfis[painelState.perfilAtivo] ?: Personagem(nome = "Principal")
        val estoque = perfilAtual.estoque

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
                        corTitulo = ColorBase
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
                        corTitulo = ColorLuxanima
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

// === COMPONENTES ESPECÍFICOS ===
                Text(
                    "COMPONENTES ESPECÍFICOS",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
                )

// Linha 1: Berkana e Thurisaz (Cabelo)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ItemEstoqueCard(
                        modifier = Modifier.weight(1f),
                        titulo = "Armadura Dullahan",
                        quantidade = estoque.armaduraDullahan,
                        meta = "Meta: 15-20",
                        corTitulo = ColorBerkana
                    ) { novaQtd ->
                        val novoEstoque = estoque.copy(armaduraDullahan = novaQtd)
                        val novoPersonagem = perfilAtual.copy(estoque = novoEstoque)
                        val novosPerfis =
                            painelState.perfis.toMutableMap().apply { put(painelState.perfilAtivo, novoPersonagem) }
                        val novoEstado = painelState.copy(perfis = novosPerfis)

                        StorageManager.salvar(novoEstado)
                        painelState = novoEstado
                    }
                    ItemEstoqueCard(
                        modifier = Modifier.weight(1f),
                        titulo = "Cabelo Azul",
                        quantidade = estoque.cabeloAzul,
                        meta = "Meta: 15-20",
                        corTitulo = ColorThurisaz
                    ) { novaQtd ->
                        val novoEstoque = estoque.copy(cabeloAzul = novaQtd)
                        val novoPersonagem = perfilAtual.copy(estoque = novoEstoque)
                        val novosPerfis =
                            painelState.perfis.toMutableMap().apply { put(painelState.perfilAtivo, novoPersonagem) }
                        val novoEstado = painelState.copy(perfis = novosPerfis)

                        StorageManager.salvar(novoEstado)
                        painelState = novoEstado
                    }
                }

// Linha 2: Thurisaz (Garra) e Luxanima (Ouro)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ItemEstoqueCard(
                        modifier = Modifier.weight(1f),
                        titulo = "Garra de Lobo",
                        quantidade = estoque.garraLobo,
                        meta = "Meta: 15-20",
                        corTitulo = ColorThurisaz
                    ) { novaQtd ->
                        val novoEstoque = estoque.copy(garraLobo = novaQtd)
                        val novoPersonagem = perfilAtual.copy(estoque = novoEstoque)
                        val novosPerfis =
                            painelState.perfis.toMutableMap().apply { put(painelState.perfilAtivo, novoPersonagem) }
                        val novoEstado = painelState.copy(perfis = novosPerfis)

                        StorageManager.salvar(novoEstado)
                        painelState = novoEstado
                    }
                    ItemEstoqueCard(
                        modifier = Modifier.weight(1f),
                        titulo = "Ouro (Mi Gao)",
                        quantidade = estoque.ouro,
                        meta = "Meta: 1-2",
                        corTitulo = ColorZeny
                    ) { novaQtd ->
                        val novoEstoque = estoque.copy(ouro = novaQtd)
                        val novoPersonagem = perfilAtual.copy(estoque = novoEstoque)
                        val novosPerfis =
                            painelState.perfis.toMutableMap().apply { put(painelState.perfilAtivo, novoPersonagem) }
                        val novoEstado = painelState.copy(perfis = novosPerfis)

                        StorageManager.salvar(novoEstado)
                        painelState = novoEstado
                    }
                }

// Linha 3: Othila (Dente)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ItemEstoqueCard(
                        modifier = Modifier.weight(1f),
                        titulo = "Dente de Ogre",
                        quantidade = estoque.denteOgre,
                        meta = "Meta: 10",
                        corTitulo = ColorOthila
                    ) { novaQtd ->
                        val novoEstoque = estoque.copy(denteOgre = novaQtd)
                        val novoPersonagem = perfilAtual.copy(estoque = novoEstoque)
                        val novosPerfis =
                            painelState.perfis.toMutableMap().apply { put(painelState.perfilAtivo, novoPersonagem) }
                        val novoEstado = painelState.copy(perfis = novosPerfis)

                        StorageManager.salvar(novoEstado)
                        painelState = novoEstado
                    }
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// === COMPONENTE COMPARTILHADO REUTILIZÁVEL PARA OS CARDS DE ITENS ===
@Composable
fun ItemEstoqueCard(
    modifier: Modifier = Modifier,
    titulo: String,
    quantidade: Int,
    meta: String,
    corTitulo: Color,
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