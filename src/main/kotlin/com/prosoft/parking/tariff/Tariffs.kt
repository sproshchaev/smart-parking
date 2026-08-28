package com.prosoft.parking.tariff

typealias Tariff = (Int) -> Int

fun fee(minutes: Int, ratePerHour: Int = 60, freeMinutes: Int = 15): Int {

    if (minutes <= freeMinutes) return 0
    val hours = (minutes + 59) / 60
    return if (hours <= 2) hours * ratePerHour
    else 2 * ratePerHour + (hours - 2) * (ratePerHour * 2 / 3)
}

val flat: Tariff = { minutes -> fee(minutes) }
val night: Tariff = { minutes -> fee(minutes, 30) }

fun withPromo(base: Tariff, persent: Int): Tariff = { minutes ->
    base(minutes) * (100 - persent) / 100
}

class Cashier(private val tariff: Tariff) {
    fun charge(minutes: Int): Int = tariff(minutes)
}
