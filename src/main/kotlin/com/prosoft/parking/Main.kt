package com.prosoft.parking

import com.prosoft.parking.model.*
import com.prosoft.parking.tariff.Cashier
import com.prosoft.parking.tariff.Tariff
import com.prosoft.parking.tariff.fee
import com.prosoft.parking.tariff.flat
import com.prosoft.parking.tariff.night
import com.prosoft.parking.tariff.withPromo

fun buildSpots(): List<Spot> = buildList {
    repeat(4) { add(Spot("L1-0${it + 1}", 1, SpotType.COMPACT)) }
    repeat(3) { add(Spot("L2-0${it + 1}", 3, SpotType.STANDARD)) }
    repeat(2) { add(Spot("L3-0${it + 1}", 3, SpotType.TRUCK)) }
}

fun main() {

    val parking = Parking(buildSpots())
    val start = 1_700_000_000_000

    val tariffs: Map<String, Tariff> = mapOf(
        "дневной" to flat,
        "ночной" to night,
        "промо -20%" to withPromo(flat, persent = 20),
        )

    tariffs.forEach { (name, tariff) ->
        println("$name: 95 мин -> ${Cashier(tariff).charge(95)} руб.")
    }

    val result = parking.enter(Car("A123BC77"), start)

    val session  = when (result) {
        is ParkResult.Ok -> result.session.also {
            println("LOG: создана сессия $it")
        }
        is ParkResult.UnknownPlate -> error("номер не распознан")
        is ParkResult.AlreadyInside -> error("авто внутри")
        ParkResult.NoSpace -> error("мест нет")
    }

    val report = StringBuilder().apply {
        appendLine("Отчет:")
        appendLine("  Мест всего: ${parking.total}")
        appendLine("  Занятость: ${parking.occupancy()}%")
        appendLine("  Сессия: ${session.plate} на ${session.spotId}")
    }.toString()
    print(report)

    val plates = parking.activeSessions().map(Session::plate)

    println("Внутри: $plates")

}


