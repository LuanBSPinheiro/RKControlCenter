package org.zera.rkcontrolcenter.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.zera.rkcontrolcenter.ui.theme.*

@Composable
fun TarefaFarmRow(
    modifier: Modifier = Modifier,
    name: String,
    rota: String,
    loot: String,
    isTest: Boolean,
    isChecked: Boolean,
    monstro: String = "", // ➕ Recebendo o monstro
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    val ehInstancia = rota == "Instância"
    val corDaBorda = when {
        isTest -> ColorTeste
        ehInstancia -> ColorInstancia
        else -> ColorFarmMapa
    }
    val corTextoRota = if (ehInstancia) ColorInstancia else ColorPrimary
    val vetorIconeRota = if (ehInstancia) IconeInstanciaNativo else IconeMapaNativo

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = ColorCardBg),
        border = BorderStroke(width = 1.5.dp, color = corDaBorda)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f).padding(start = 16.dp, end = 8.dp)
            ) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Rota / Mapa
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = vetorIconeRota,
                        contentDescription = null,
                        tint = corTextoRota,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = rota,
                        color = corTextoRota,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // ➕ NOVO: Se houver monstro especificado, exibe com destaque cinza claro
                if (monstro.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "🎯 Alvo: $monstro",
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = loot,
                    color = ColorMeta,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }

            // Botão Lixeira
            IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = IconeDeletarNativo, contentDescription = "Remover Atividade", modifier = Modifier.size(18.dp), tint = Color(0xFFF75A5B))
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Checkbox
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(end = 16.dp),
                colors = CheckboxDefaults.colors(checkedColor = ColorPrimary, uncheckedColor = Color.White.copy(alpha = 0.6f))
            )
        }
    }
}