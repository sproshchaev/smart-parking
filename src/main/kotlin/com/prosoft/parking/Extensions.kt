package com.prosoft.parking

import com.prosoft.parking.model.Spot
import com.prosoft.parking.model.Vehicle

fun Int.asFee(ratePerHour: Int = 60): Int = (this + 59) / 60 * ratePerHour

val Vehicle.shortPlate: String get() = plate.take(6)

infix fun Vehicle.parksAt(spot: Spot) {
    spot.occupy(this)
}

inline fun <reified T : Vehicle> List<Vehicle>.only(): List<T> = filterIsInstance<T>()

