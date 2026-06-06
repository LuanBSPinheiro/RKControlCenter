package org.zera.rkcontrolcenter.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.zera.rkcontrolcenter.ui.theme.*

@Composable
fun WalletZenyCard(
    zenyAtual: Long,
    zenyInputText: String,
    onInputChange: (String) -> Unit,
    onSalvarClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ColorCardBg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("CARTEIRA DE ZENY", color = ColorZeny, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            val zenyFormatado = zenyAtual.toString().reversed().chunked(3).joinToString(".").reversed()
            Text(
                text = "$zenyFormatado z",
                color = ColorZeny,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = zenyInputText,
                    onValueChange = onInputChange,
                    label = { Text("Atualizar Zeny total") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onSalvarClick,
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("Salvar", color = ColorBackground, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}