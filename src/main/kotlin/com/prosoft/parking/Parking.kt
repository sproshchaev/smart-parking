package com.prosoft.parking

import com.prosoft.parking.model.*

class Parking(private val spots: List<Spot>) {

    private val active = mutableMapOf<String, Session>()

    val total: Int get() = spots.size

    fun enter(vehicle: Vehicle, now: Long): ParkResult {

        val spot = spots
            .filter { it.isFree && it.fits(vehicle) }
            .sortedWith(
                compareBy(
                    { it.type.maxSizeFactor },
                    { it.level }, { it.id })
            )
            .firstOrNull() ?: return ParkResult.NoSpace

        spot.occupy(vehicle)
        val session = Session.start(vehicle, spot, now)
        active[vehicle.plate] = session
        return ParkResult.Ok(session)
    }

    // Методы для отчетов: все 4 возвращают List и Map (только для чтения)
    fun freeSpots(type: SpotType): List<Spot> = spots.filter {
        it.isFree && it.type == type
    }

                                          // плоский список -> уровень: список мест
    fun byLevel(): Map<Int, List<Spot>> = spots.groupBy { it.level }

    fun occupancy(): Int = spots.count { !it.isFree } * 100 / spots.size

    // Сортировка по времени въезда
    fun activeSessions(): List<Session> = active.values.sortedBy { it.startedAt }

    // Выезд
    fun exit(plate: String, now: Long): Pair<Session, Int>? {
                                               // Элвис (есть/null)
        val session = active.remove(plate) ?: return null
        spots.first { it.id == session.spotId }.release()
        val minutes = ((now - session.startedAt) / 60_000).toInt() // 60_000 мс в мин
        return session to minutes // инфикс to
    }

}