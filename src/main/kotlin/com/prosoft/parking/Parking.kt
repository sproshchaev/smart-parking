package com.prosoft.parking

import com.prosoft.parking.model.*
import com.prosoft.parking.plate.recognizePlate

class Parking(private val spots: List<Spot>) {

    private val active = mutableMapOf<String, Session>()

    val total: Int get() = spots.size

    fun enter(vehicle: Vehicle, now: Long): ParkResult {

        // проверка аргумента
        require(vehicle.plate.isNotBlank()) {
            "Номер не может быть пустым"
        }

        // проверка повторного въезда
        if (active.containsKey(vehicle.plate))
            return ParkResult.AlreadyInside(vehicle.plate)

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

    // въезд по сырому номеру
    fun enterByPlate(raw: String?, now: Long, factory: (String) -> Vehicle): ParkResult {
        val plate = recognizePlate(raw) ?: return ParkResult.UnknownPlate(raw)
        return enter(factory(plate.value), now)
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

    // Обертка над въездом
    fun enterChecked(vehicle: Vehicle, now: Long): Result<Session> = runCatching {
        when (val result = enter(vehicle, now)) {
            is ParkResult.Ok -> result.session
            is ParkResult.AlreadyInside -> error("${result.plate} уже на парковке")
            is ParkResult.UnknownPlate -> error("Номер не распознан: ${result.raw}")
            ParkResult.NoSpace -> error("Свободных мест нет")
        }
    }

    // Выезд
    fun exit(plate: String, now: Long): Pair<Session, Int> {
                                               // Элвис (есть/null)
        val session = active.remove(plate) ?: throw SessionNotFoundException(plate)
        spots.first { it.id == session.spotId }.release()
        check(now >= session.startedAt) { "Время выезда раньше время въезда" }
        val minutes = ((now - session.startedAt) / 60_000).toInt() // 60_000 мс в мин
        return session to minutes // инфикс to
    }

}