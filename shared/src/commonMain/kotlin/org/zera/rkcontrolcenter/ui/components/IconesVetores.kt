package org.zera.rkcontrolcenter.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IconeAdicionarNativo: ImageVector by lazy {
    ImageVector.Builder(name = "Add", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f)
        .path(fill = SolidColor(Color.White)) {
            moveTo(19f, 13f)
            lineTo(13f, 13f)
            lineTo(13f, 19f)
            lineTo(11f, 19f)
            lineTo(11f, 13f)
            lineTo(5f, 13f)
            lineTo(5f, 11f)
            lineTo(11f, 11f)
            lineTo(11f, 5f)
            lineTo(13f, 5f)
            lineTo(13f, 11f)
            lineTo(19f, 11f)
            close()
        }.build()
}

val IconeDeletarNativo: ImageVector by lazy {
    ImageVector.Builder(name = "Delete", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f)
        .path(fill = SolidColor(Color.Red)) {
            moveTo(6f, 19f)
            curveTo(6f, 20.1f, 6.9f, 21f, 8f, 21f)
            lineTo(16f, 21f)
            curveTo(17.1f, 21f, 18f, 20.1f, 18f, 19f)
            lineTo(18f, 7f)
            lineTo(6f, 7f)
            lineTo(6f, 19f)
            close()
            moveTo(19f, 4f)
            lineTo(15.5f, 4f)
            lineTo(14.5f, 3f)
            lineTo(9.5f, 3f)
            lineTo(8.5f, 4f)
            lineTo(5f, 4f)
            lineTo(5f, 6f)
            lineTo(19f, 6f)
            close()
        }.build()
}

// Vetor nativo para o ícone de Check (✓)
val IconeCheckNativo: ImageVector by lazy {
    ImageVector.Builder(name = "Check", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f)
        .path(fill = SolidColor(Color.White)) {
            moveTo(9f, 16.17f)
            lineTo(4.83f, 12f)
            lineTo(3.41f, 13.41f)
            lineTo(9f, 19f)
            lineTo(21f, 7f)
            lineTo(19.59f, 5.59f)
            lineTo(9f, 16.17f)
            close()
        }.build()
}

// Vetor nativo para o ícone de Dinheiro (reaproveitando o desenho vetorial de 'check')
val IconeDinheiroNativo: ImageVector by lazy {
    ImageVector.Builder(name = "Money", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f)
        .path(fill = SolidColor(Color.White)) {
            // Reaproveita o desenho vetorial de 'check' para manter o estilo visual
            moveTo(9f, 16.17f)
            lineTo(4.83f, 12f)
            lineTo(3.41f, 13.41f)
            lineTo(9f, 19f)
            lineTo(21f, 7f)
            lineTo(19.59f, 5.59f)
            lineTo(9f, 16.17f)
            close()
        }.build()
}

// Vetor nativo customizado: Símbolo de Zeny (Z com corte vertical '|')
val IconeMoedaRodapeNativo: ImageVector by lazy {
    ImageVector.Builder(
        name = "ZenySymbol",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFFFFD700)),
        strokeLineWidth = 2.5f, // 📐 Deixa o traço do Zeny bem nítido e robusto
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        // --- Desenho do 'Z' ---
        moveTo(6f, 6f)       // Canto superior esquerdo do Z
        lineTo(18f, 6f)     // Linha superior até a direita
        lineTo(6f, 18f)     // Diagonal descendo até o canto inferior esquerdo
        lineTo(18f, 18f)    // Linha inferior até a direita

        // --- Desenho da Barra Vertical '|' que corta o Z ---
        moveTo(12f, 3f)     // Início um pouco acima do Z
        lineTo(12f, 21f)    // Reta descendo até o final para dar o efeito de cifrão
    }.build()
}