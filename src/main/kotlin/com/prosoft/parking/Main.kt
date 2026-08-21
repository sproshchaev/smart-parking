package com.prosoft.parking

import com.prosoft.parking.model.*
import com.prosoft.parking.tariff.fee

fun main() {



    val car = Car("A123BC77")
    val spot = Spot("L1-01", level = 1, SpotType.COMPACT)
    val session = Session.start(car, spot, now = 1_700_000_000_000)

    println(session) // Session(plate=A123BC77, spotId=L1-01, startedAt=1700000000000)

    val (plate, spotId, startAt) = session // деструктуризация

    println("Номер $plate, место $spotId, старт $startAt")

    println(session == session.copy())

    println(session.copy(spotId = "L2-07"))

    listOf(
        ParkResult.Ok(session),
        ParkResult.NoSpace,
        ParkResult.UnknownPlate(" ??? "),
    ).forEach { result ->
        val message = when (result) {
            is ParkResult.Ok -> "Место ${result.session.spotId} ваше"
            is ParkResult.UnknownPlate -> "Не читаю номер: ${result.raw}"
            ParkResult.NoSpace -> "Свободных мест нет!"
        }
        println(message)
    }

    SpotType.entries.forEach {
        println("${it.name}: ${it.title}, до ${it.maxSizeFactor}")
    }

}


