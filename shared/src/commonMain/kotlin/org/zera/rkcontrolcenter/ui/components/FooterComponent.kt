package org.zera.rkcontrolcenter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.zera.rkcontrolcenter.ui.theme.*

@Composable
fun FooterComponent(onQrCodeClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 40.dp), // Espaçamento reforçado no topo
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = ColorCardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Créditos de Criação
            Text(
                text = "Desenvolvido com ⚔️ por Zera",
                color = Color.LightGray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            HorizontalDivider(
                color = Color.DarkGray,
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Criando o texto misto com link clicável interno
            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.White)) {
                    append("Curtiu o painel? Ajude a manter o projeto ativo e pague um cafézinho para o ferreiro ")
                }

                // Trecho clicável e destacado com a cor do Zeny
                pushStringAnnotation(tag = "URL", annotation = "pix")
                withStyle(style = SpanStyle(color = ColorZeny, fontWeight = FontWeight.Bold)) {
                    append("clicando AQUI!")
                }
                pop()
            }

            ClickableText(
                text = annotatedString,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                ),
                onClick = { offset ->
                    // Verifica se o usuário clicou exatamente onde colocamos a tag "URL"
                    annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                        .firstOrNull()?.let {
                            onQrCodeClick() // Dispara a ação de abrir o popup
                        }
                }
            )
        }
    }
}