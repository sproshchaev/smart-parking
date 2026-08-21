package com.prosoft.parking

import com.prosoft.parking.model.ParkResult
import com.prosoft.parking.model.Session
import com.prosoft.parking.model.Spot
import com.prosoft.parking.model.Vehicle

class Parking(private val spots: List<Spot>) {

    private val active = mutableMapOf<String, Session>()

    val total: Int get() = spots.size

    fun enter(vehicle: Vehicle, now: Long): ParkResult {

        val spot = spots
            .filter { it.isFree && it.fits(vehicle) }
            .sortedWith(compareBy( {it.type.maxSizeFactor },
                { it.level }, { it.id }))
            .firstOrNull() ?: return ParkResult.NoSpace

        spot.occupy(vehicle)
        val session = Session.start(vehicle, spot, now)
        active[vehicle.plate] = session
        return ParkResult.Ok(session)
    }
}