package org.zera.rkcontrolcenter.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.zera.rkcontrolcenter.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAddCronograma(
    onDismiss: () -> Unit,
    onConfirm: (nome: String, tipoInstancia: Boolean, rota: String, loot: String, monstro: String, ehTeste: Boolean) -> Unit
) {
    var tipoInstancia by remember { mutableStateOf(true) }
    var nomeAtividade by remember { mutableStateOf("") }
    var infoExtra by remember { mutableStateOf("") } // Loot ou Rota
    var localAtividade by remember { mutableStateOf("") } // Nome do Mapa
    var ehTeste by remember { mutableStateOf(false) }
    var nomeMonstro by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova Atividade Diária", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Selecione o tipo de rota para o cronograma:", color = Color.LightGray, fontSize = 13.sp)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = tipoInstancia,
                        onClick = { tipoInstancia = true },
                        label = { Text("Instância", fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ColorInstancia,
                            selectedLabelColor = ColorBackground
                        )
                    )
                    FilterChip(
                        selected = !tipoInstancia,
                        onClick = { tipoInstancia = false },
                        label = { Text("Mapa de Farm", fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ColorPrimary,
                            selectedLabelColor = ColorBackground
                        )
                    )
                }

                // Input de Nome Principal
                OutlinedTextField(
                    value = nomeAtividade,
                    onValueChange = { nomeAtividade = it },
                    label = { Text("Nome (Ex: Memórias de Sara)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Inputs condicionados
                if (tipoInstancia) {
                    OutlinedTextField(
                        value = infoExtra,
                        onValueChange = { infoExtra = it },
                        label = { Text("Descrição / O que buscar lá dentro?") },
                        placeholder = { Text("Ex: Guardar comidas para vender ou usar") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    OutlinedTextField(
                        value = localAtividade,
                        onValueChange = { localAtividade = it },
                        label = { Text("Código do Mapa (Ex: spl_fild02)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nomeMonstro,
                        onValueChange = { nomeMonstro = it },
                        label = { Text("Monstro a ser caçado (Ex: Pinguicula)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = infoExtra,
                        onValueChange = { infoExtra = it },
                        label = { Text("Rota de Acesso ou Drop principal") },
                        placeholder = { Text("Ex: Vá para Esplendor -> 1 mapa à esquerda") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Toggle de Teste
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                ) {
                    Checkbox(
                        checked = ehTeste,
                        onCheckedChange = { ehTeste = it },
                        colors = CheckboxDefaults.colors(checkedColor = ColorTeste)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Marcar como Teste de Build", color = Color.LightGray, fontSize = 13.sp)
                }
            }
        },
        containerColor = ColorCardBg,
        confirmButton = {
            Button(
                onClick = {
                    val nomeLimpo = nomeAtividade.trim()
                    if (nomeLimpo.isNotEmpty()) {
                        onConfirm(
                            nomeLimpo,
                            tipoInstancia,
                            if (tipoInstancia) "Instância" else localAtividade.trim(),
                            infoExtra.trim(),
                            if (tipoInstancia) "" else nomeMonstro.trim(),
                            ehTeste
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
            ) {
                Text("Adicionar", color = ColorBackground, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}