package org.zera.rkcontrolcenter.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private val IconSize = 24.dp
private const val IconViewport = 24f

val IconeAdicionarNativo: ImageVector by lazy {
    filledIcon(name = "Add") {
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
    }
}

val IconeDeletarNativo: ImageVector by lazy {
    filledIcon(name = "Delete", fill = Color.Red) {
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
    }
}

val IconeCheckNativo: ImageVector by lazy {
    filledIcon(name = "Check") {
        checkPath()
    }
}

val IconeDinheiroNativo: ImageVector by lazy {
    filledIcon(name = "Money") {
        checkPath()
    }
}

val IconeMoedaRodapeNativo: ImageVector by lazy {
    strokedIcon(name = "ZenySymbol", stroke = Color(0xFFFFD700), strokeLineWidth = 2.5f) {
        moveTo(6f, 6f)
        lineTo(18f, 6f)
        lineTo(6f, 18f)
        lineTo(18f, 18f)

        moveTo(12f, 3f)
        lineTo(12f, 21f)
    }
}

// ➕ Ícone de Instância padronizado (Sintaxe Corrigida)
val IconeInstanciaNativo: ImageVector by lazy {
    filledIcon(name = "Dungeon") {
        moveTo(12f, 2f)
        lineTo(4f, 5f)
        verticalLineTo(15f) // 👈 Alterado de v para verticalLineTo
        lineTo(12f, 22f)
        lineTo(20f, 19f)
        verticalLineTo(5f)  // 👈 Nota: o SVG original usava v(-14f) relativo. Convertido para a coordenada absoluta 5f
        close()

        moveTo(18f, 17f)
        lineTo(12f, 19.5f)
        lineTo(6f, 17.3f)
        verticalLineTo(7.3f) // 👈 Convertido de v(-10f) relativo para a coordenada absoluta 7.3f
        lineTo(12f, 4.5f)
        lineTo(18f, 7f)
        close()
    }
}

// ➕ Ícone de Mapa de Farm padronizado (Sintaxe Corrigida)
val IconeMapaNativo: ImageVector by lazy {
    filledIcon(name = "Map") {
        moveTo(20.5f, 3f)
        lineTo(15f, 5.1f)
        lineTo(9f, 3f)
        lineTo(3.5f, 5.1f)
        verticalLineTo(21f) // 👈 Convertido de v(15.9f) relativo para a coordenada absoluta 21f
        lineTo(9f, 18.9f)
        lineTo(15f, 21f)
        lineTo(20.5f, 18.9f)
        close()

        moveTo(15f, 18.9f)
        lineTo(9f, 16.8f)
        verticalLineTo(3f)   // 👈 Convertido de v(-13.8f) relativo para a coordenada absoluta 3f
        lineTo(15f, 15f)
        close()
    }
}

// ➕ NOVO: Ícone de Reset Diário padronizado
val IconeResetNativo: ImageVector by lazy {
    filledIcon(name = "Reset") {
        moveTo(17.65f, 6.35f)
        curveTo(16.2f, 4.9f, 14.21f, 4f, 12f, 4f)
        curveTo(7.58f, 4f, 4.01f, 7.58f, 4.01f, 12f)
        horizontalLineTo(7f)
        curveTo(7f, 9.24f, 9.24f, 7f, 12f, 7f)
        curveTo(13.38f, 7f, 14.63f, 7.56f, 15.54f, 8.46f)
        lineTo(12f, 12f)
        horizontalLineTo(21f)
        verticalLineTo(3f)
        close()
    }
}

private fun filledIcon(
    name: String,
    fill: Color = Color.White,
    path: PathBuilder.() -> Unit
): ImageVector = iconBuilder(name)
    .path(fill = SolidColor(fill)) {
        path()
    }
    .build()

private fun strokedIcon(
    name: String,
    stroke: Color,
    strokeLineWidth: Float,
    path: PathBuilder.() -> Unit
): ImageVector = iconBuilder(name)
    .path(
        fill = null,
        stroke = SolidColor(stroke),
        strokeLineWidth = strokeLineWidth,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        path()
    }
    .build()

private fun iconBuilder(name: String) = ImageVector.Builder(
    name = name,
    defaultWidth = IconSize,
    defaultHeight = IconSize,
    viewportWidth = IconViewport,
    viewportHeight = IconViewport
)

private fun PathBuilder.checkPath() {
    moveTo(9f, 16.17f)
    lineTo(4.83f, 12f)
    lineTo(3.41f, 13.41f)
    lineTo(9f, 19f)
    lineTo(21f, 7f)
    lineTo(19.59f, 5.59f)
    lineTo(9f, 16.17f)
    close()
}