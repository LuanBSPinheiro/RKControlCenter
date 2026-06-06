package org.zera.rkcontrolcenter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.zera.rkcontrolcenter.Personagem
import org.zera.rkcontrolcenter.domain.MatrizCraft
import org.zera.rkcontrolcenter.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RunePlannerSection(
    perfil: Personagem,
    quantidadeDesejadaMap: MutableMap<String, String>,
    coresRunas: Map<String, Color>
) {
    val estoque = perfil.estoque

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = IconeResetNativo,
                contentDescription = null,
                tint = ColorPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "CALCULADOR DE RUNAS (PLANEJAMENTO)",
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val colunas = if (maxWidth < 650.dp) 1 else 4
            val larguraCard = (maxWidth - (12.dp * (colunas - 1))) / colunas

            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                maxItemsInEachRow = colunas
            ) {
                MatrizCraft.receitasRunas.forEach { (nomeRuna, receita) ->
                    val inputText = quantidadeDesejadaMap[nomeRuna] ?: ""
                    val qtdDesejada = inputText.toIntOrNull() ?: 1
                    val result = MatrizCraft.calcularCraft(estoque, receita, qtdDesejada)
                    val podeCraftar = result.faltantesDetalhados.isEmpty() && qtdDesejada > 0
                    val corTema = coresRunas[nomeRuna] ?: White

                    Card(
                        modifier = Modifier.width(larguraCard).heightIn(min = 160.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = ColorCardBg)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
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
                                OutlinedTextField(
                                    value = inputText,
                                    onValueChange = { nv ->
                                        if (nv.all { it.isDigit() }) quantidadeDesejadaMap[nomeRuna] = nv
                                    },
                                    placeholder = { Text("1", fontSize = 11.sp, color = ColorMeta) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.width(55.dp).height(48.dp),
                                    textStyle = TextStyle(fontSize = 12.sp, color = White, textAlign = TextAlign.Center),
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
                                    Box(modifier = Modifier.size(8.dp).background(ColorPrimary, shape = CircleShape))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "Pronto para criar $qtdDesejada!", color = ColorPrimary, fontSize = 13.sp)
                                }
                            } else {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(8.dp).background(Color.Red, shape = CircleShape))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "Falta para $qtdDesejada runa(s):", color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))

                                    result.faltantesDetalhados.forEach { detalhe ->
                                        val material = detalhe.first
                                        val unitsMissing = detalhe.second
                                        val precoBazar = estoque.precosMercado[material] ?: 0
                                        val itemCost = unitsMissing.toLong() * precoBazar
                                        val formattedCost = if (itemCost > 0L) {
                                            " " + itemCost.toString().reversed().chunked(3).joinToString(".").reversed() + " z"
                                        } else ""

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

                                    if (result.custoTotalZeny > 0) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))
                                        val zenyTotalFormatado = result.custoTotalZeny.toString().reversed().chunked(3).joinToString(".").reversed()

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = IconeMoedaRodapeNativo, contentDescription = null, tint = ColorZeny, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "Custo Total: $zenyTotalFormatado z", color = ColorZeny, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}