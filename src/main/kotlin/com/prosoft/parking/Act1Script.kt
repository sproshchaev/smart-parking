package com.prosoft.parking

fun fee(minutes: Int, ratePerHour: Int = 60, freeMinutes: Int = 15): Int {
    if (minutes <= freeMinutes) return 0
    val hours = (minutes + 59) / 60
    return if (hours <= 2) hours * ratePerHour
    else 2 * ratePerHour + (hours - 2) * (ratePerHour * 2 / 3)
}

fun recognizePlate(raw: String?): String? =
    raw?.trim()?.uppercase()?.takeIf { it.length in 8..9 }

fun main() {
    val fromCamera: String? = " а123вс77 "
    val plate = recognizePlate(fromCamera)

    if (plate != null) {
        println("Открываем шлагбаум: $plate, ${plate.length} символов")
    } else {
        println("Вызов оператора") // TODO
    }

    println("Табло: " + (recognizePlate(null) ?: "Не распознан!"))
    println("Табло: " + (recognizePlate("грязь") ?: "Не распознан!"))

    println("К оплате: ${fee(95)} руб.");

}

