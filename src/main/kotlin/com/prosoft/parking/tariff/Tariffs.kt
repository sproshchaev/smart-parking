package com.prosoft.parking.tariff

fun fee(minutes: Int, ratePerHour: Int = 60, freeMinutes: Int = 15): Int {
    if (minutes <= freeMinutes) return 0
    val hours = (minutes + 59) / 60
    return if (hours <= 2) hours * ratePerHour
    else 2 * ratePerHour + (hours - 2) * (ratePerHour * 2 / 3)
}