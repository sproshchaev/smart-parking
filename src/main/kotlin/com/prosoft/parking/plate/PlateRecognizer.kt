package com.prosoft.parking.plate

data class Plate(val value: String, val region: String)

private const val LETTERS = "АВЕКМНОРСТУХ"

private val PLATE = Regex(
    """^(?<series1>[$LETTERS])(?<digits>\d{3})(?<series2>[$LETTERS]{2})(?<region>\d{2,3})$"""
)

private val PLATE_IN_TEXT = Regex("""[$LETTERS]\d{3}[$LETTERS]{2}\d{2,3}""")

fun recognizePlate(raw: String?): Plate? {
    val cleaned = raw?.trim()?.uppercase()?.replace(" ", "") ?: return null
    val match = PLATE.matchEntire(cleaned) ?: return null
    return Plate(cleaned, match.groups["region"]?.value ?: "00")
}

fun platesInLog(line: String): List<Plate> = PLATE_IN_TEXT.findAll(line).mapNotNull {
    recognizePlate(it.value)
}.toList()