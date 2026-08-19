package com.prosoft.parking.model

class Spot(val id: String, val level: Int) {

    var occupiedBy: Vehicle? = null
        private set

    val isFree: Boolean get() = occupiedBy == null

    fun occupy(vehicle: Vehicle) {
        occupiedBy = vehicle
    }

    fun release() {
        occupiedBy = null
    }

}