package org.zera.rkcontrolcenter.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.zera.rkcontrolcenter.ui.theme.*
import org.jetbrains.compose.resources.painterResource
import rkcontrolcenter.shared.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogPixQrCode(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Pague um Cafézinho! ☕",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Aponte a câmera do seu banco para o QR Code abaixo ou copie a chave Pix por escrito.",
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                // Renderiza a sua imagem do QR Code gerada pelo projeto
                Image(
                    painter = painterResource(Res.drawable.qrcode_pix),
                    contentDescription = "QR Code Pix",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(8.dp)
                )

                // Campo complementar com a chave escrita (Copia e Cola)
                Card(
                    colors = CardDefaults.cardColors(containerColor = ColorBackground),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Chave PIX (E-mail):",
                            color = Color.Gray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "luanspinheiro@gmail.com",
                            color = ColorZeny,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        containerColor = ColorCardBg,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Fechar", color = ColorBackground, fontWeight = FontWeight.Bold)
            }
        }
    )
}