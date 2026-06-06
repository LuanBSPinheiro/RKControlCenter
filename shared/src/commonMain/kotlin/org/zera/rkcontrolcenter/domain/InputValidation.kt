package org.zera.rkcontrolcenter.domain

const val MaxProfileNameLength = 32
const val MaxActivityNameLength = 80
const val MaxRouteLength = 80
const val MaxActivityDescriptionLength = 240
const val MaxNumericInputLength = 12
const val MaxZenyInputLength = 15

private val CollapsedWhitespace = Regex("\\s+")

fun String.onlyDigits(maxLength: Int = MaxNumericInputLength): String =
    filter { it.isDigit() }.take(maxLength)

fun String.toNonNegativeIntOrNull(): Int? =
    trim().toIntOrNull()?.takeIf { it >= 0 }

fun String.toNonNegativeLongOrNull(): Long? =
    trim().toLongOrNull()?.takeIf { it >= 0L }

fun String.sanitizedSingleLine(maxLength: Int): String =
    filterNot { it.isControlCharacter() }
        .trim()
        .replace(CollapsedWhitespace, " ")
        .take(maxLength)

private fun Char.isControlCharacter(): Boolean =
    code < 32 || code == 127
