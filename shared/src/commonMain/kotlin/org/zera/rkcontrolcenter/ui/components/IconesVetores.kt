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
