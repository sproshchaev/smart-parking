package com.prosoft.parking.model

import com.prosoft.parking.repo.Identifiable

sealed interface ParkResult {
    data class Ok(val session: Session) : ParkResult
    data class UnknownPlate(val raw: String?) : ParkResult
    data class AlreadyInside(val plate: String) : ParkResult
    data object NoSpace: ParkResult
}

data class Session(val plate: String, val spotId: String, val startedAt: Long) : Identifiable<String> {

    override val id: String = plate

    companion object {
        fun start(vehicle: Vehicle, spot: Spot, now: Long)
        = Session(vehicle.plate, spot.id, now);
    }
}