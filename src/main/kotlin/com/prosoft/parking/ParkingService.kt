package com.prosoft.parking

import com.prosoft.parking.model.Car
import com.prosoft.parking.model.Clock
import com.prosoft.parking.model.ParkResult
import com.prosoft.parking.model.Session
import com.prosoft.parking.model.SystemClock
import com.prosoft.parking.model.Truck
import com.prosoft.parking.storage.SessionLog
import com.prosoft.parking.tariff.Tariff
import com.prosoft.parking.tariff.flat

data class Receipt(val plate: String, val spotId: String, val minutes: Int, val amount: Int)

class ParkingService(
    private val parking: Parking,
    private val log: SessionLog,
    private val clock: Clock = SystemClock,
    private val tariff: Tariff = flat,
    ) {

    fun enter(rawPlate: String, axles: Int? = null): Session {
        val result = parking.enterByPlate(rawPlate, clock.now()) {
            plate -> if (axles == null) Car(plate) else Truck(plate, axles)
        }
        return when (result) {
            is ParkResult.Ok -> result.session
            is ParkResult.UnknownPlate -> throw IllegalArgumentException("Номер не распознан: ${result.raw}")
            is ParkResult.AlreadyInside -> throw IllegalStateException("${result.plate} уже на парковке")
            is ParkResult.NoSpace -> throw IllegalArgumentException("Свободных мест нет")
        }
    }

    fun exit(plate: String): Receipt {
        val (session, minutes) = parking.exit(plate, clock.now())
        val amount = tariff(minutes)
        log.append(session, minutes, amount)
        return Receipt(session.plate, session.spotId, minutes, amount)
    }

    fun active(): List<Session> = parking.activeSessions()

    fun revenue(): Int = log.revenue()

}