package org.zera.rkcontrolcenter.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.zera.rkcontrolcenter.PainelData
import org.zera.rkcontrolcenter.Personagem
import org.zera.rkcontrolcenter.ui.theme.ColorBackground
import org.zera.rkcontrolcenter.ui.theme.ColorCardBg
import org.zera.rkcontrolcenter.ui.theme.ColorMeta
import org.zera.rkcontrolcenter.ui.theme.ColorPrimary
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeletorPerfis(
    painelState: PainelData,
    onPerfilSelecionado: (PainelData) -> Unit
) {
    var mostrarDialogCriacao by remember { mutableStateOf(false) }
    var novoNomePerfil by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ColorCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("PERSONAGEM ATIVO", color = ColorMeta, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    painelState.perfis.keys.forEach { nomePerfil ->
                        val isActive = painelState.perfilAtivo == nomePerfil
                        FilterChip(
                            selected = isActive,
                            onClick = { onPerfilSelecionado(painelState.copy(perfilAtivo = nomePerfil)) },
                            label = { Text(nomePerfil, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ColorPrimary,
                                selectedLabelColor = ColorBackground,
                                labelColor = Color.LightGray
                            )
                        )
                    }

                    IconButton(onClick = { mostrarDialogCriacao = true }) {
                        Icon(IconeAdicionarNativo, contentDescription = "Adicionar Alt", tint = ColorPrimary)
                    }
                }
            }

            if (painelState.perfis.size > 1) {
                IconButton(
                    onClick = {
                        val perfilParaRemover = painelState.perfilAtivo
                        val chavesRestantes = painelState.perfis.keys.filter { it != perfilParaRemover }
                        val novaChaveAtiva = chavesRestantes.first()
                        val novosPerfis = painelState.perfis.toMutableMap().apply { remove(perfilParaRemover) }
                        onPerfilSelecionado(painelState.copy(perfilAtivo = novaChaveAtiva, perfis = novosPerfis))
                    }
                ) {
                    Icon(IconeDeletarNativo, contentDescription = "Remover Personagem", tint = Color.Red.copy(alpha = 0.7f))
                }
            }
        }
    }

    if (mostrarDialogCriacao) {
        AlertDialog(
            onDismissRequest = { mostrarDialogCriacao = false; novoNomePerfil = "" },
            title = { Text("Criar Novo Perfil de Alt", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            text = {
                Column {
                    Text("Insira o nome do seu novo Cavaleiro Rúnico ou Alt:", color = Color.LightGray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = novoNomePerfil,
                        onValueChange = { novoNomePerfil = it },
                        label = { Text("Nome do Personagem") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            containerColor = ColorCardBg,
            confirmButton = {
                Button(
                    onClick = {
                        val nomeLimpo = novoNomePerfil.trim()
                        if (nomeLimpo.isNotEmpty() && !painelState.perfis.containsKey(nomeLimpo)) {
                            val novosPerfis = painelState.perfis.toMutableMap().apply { put(nomeLimpo, Personagem(nome = nomeLimpo)) }
                            onPerfilSelecionado(painelState.copy(perfilAtivo = nomeLimpo, perfis = novosPerfis))
                            mostrarDialogCriacao = false
                            novoNomePerfil = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(
                        imageVector = IconeCheckNativo,
                        contentDescription = null,
                        tint = ColorBackground,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Criar Perfil", color = ColorBackground, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogCriacao = false; novoNomePerfil = "" }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }
}