package com.prosoft.parking.model

enum class SpotType(val maxSizeFactor: Double, val title: String) {
    COMPACT(1.0, "компакт"),
    STANDARD(1.5, "стандарт"),
    TRUCK(3.0, "грузовое"),
}

class Spot(val id: String, val level: Int, val type: SpotType) {

    var occupiedBy: Vehicle? = null
        private set

    val isFree: Boolean get() = occupiedBy == null

    fun fits(vehicle: Vehicle): Boolean = vehicle.sizeFactor <= type.maxSizeFactor

    fun occupy(vehicle: Vehicle) {
        occupiedBy = vehicle
    }

    fun release() {
        occupiedBy = null
    }

}