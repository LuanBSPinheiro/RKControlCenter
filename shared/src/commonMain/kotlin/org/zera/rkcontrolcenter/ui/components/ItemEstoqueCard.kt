package org.zera.rkcontrolcenter.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.zera.rkcontrolcenter.ui.theme.*

@Composable
fun ItemEstoqueCard(
    modifier: Modifier = Modifier,
    titulo: String,
    quantidade: Int,
    meta: String,
    corTitulo: Color,
    icone: DrawableResource,
    precoBazar: Int? = null,
    onQuantidadeMudou: (Int) -> Unit,
    onPrecoMudou: ((Int) -> Unit)? = null
) {
    var textInput by remember(quantidade) { mutableStateOf("") }
    var precoInput by remember(precoBazar) { mutableStateOf(precoBazar?.toString() ?: "") }

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
            Image(
                painter = painterResource(icone),
                contentDescription = titulo,
                modifier = Modifier.size(52.dp).padding(bottom = 4.dp)
            )

            Text(text = titulo, color = corTitulo, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(text = quantidade.toString(), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 2.dp))
            Text(text = meta, color = ColorMeta, fontSize = 11.sp, modifier = Modifier.padding(bottom = 6.dp))

            // Seção 1: Atualizar Quantidade de Inventário
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text("Qtd Farm", fontSize = 10.sp) }, //Label flutuante evita cortes
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f).height(54.dp), //Altura estável para a Web
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp, color = Color.White),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorPrimary,
                        unfocusedBorderColor = Color.DarkGray,
                        focusedLabelColor = ColorPrimary,
                        unfocusedLabelColor = ColorMeta
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Button(
                    onClick = {
                        val novaQtd = textInput.toIntOrNull()
                        if (novaQtd != null) { onQuantidadeMudou(novaQtd); textInput = "" }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(38.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(IconeCheckNativo, contentDescription = "Salvar Qtd", tint = ColorBackground, modifier = Modifier.size(16.dp))
                }
            }

            // Seção 2: Entrada do preço do Bazar
            if (precoBazar != null && onPrecoMudou != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = precoInput,
                        onValueChange = { precoInput = it },
                        label = { Text("Preço Bazar", fontSize = 10.sp) }, //Indicador claro do Bazar
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).height(54.dp), //Alinhado com o de cima
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp, color = ColorZeny),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorZeny,
                            unfocusedBorderColor = Color.DarkGray,
                            focusedLabelColor = ColorZeny,
                            unfocusedLabelColor = ColorMeta
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = {
                            val novoPreco = precoInput.toIntOrNull()
                            if (novoPreco != null) { onPrecoMudou(novoPreco) }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorZeny),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(38.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Icon(
                            imageVector = IconeDinheiroNativo,
                            contentDescription = "Salvar Preço",
                            tint = ColorBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}