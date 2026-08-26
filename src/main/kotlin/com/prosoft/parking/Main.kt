package com.prosoft.parking

import com.prosoft.parking.model.*
import com.prosoft.parking.tariff.fee

fun buildSpots(): List<Spot> = buildList {
    repeat(4) { add(Spot("L1-0${it + 1}", 1, SpotType.COMPACT)) }
    repeat(3) { add(Spot("L2-0${it + 1}", 3, SpotType.STANDARD)) }
    repeat(2) { add(Spot("L3-0${it + 1}", 3, SpotType.TRUCK)) }
}

fun main() {

    val parking = Parking(buildSpots())
    val start = 1_700_000_000_000

    listOf(Car("A123BC77"), Motorcycle("E789KM50"), Truck("B456EK99", 3))
        .forEach { vehicle ->
            when (val result = parking.enter(vehicle, start)) { // объявление переменной в заголовке
                is ParkResult.Ok -> println("${vehicle.plate} -> место ${result.session.spotId}")
                is ParkResult.UnknownPlate -> println("Номер не распознан: ${result.raw}")
                is ParkResult.NoSpace -> println("${vehicle.plate} -> мест нет")
            }

        }

    // Сводка по уровням и выезд с расчетом
    println("Занятость: ${parking.occupancy()}%")

    parking.byLevel().forEach { (level, spots) ->
        println("Уровень $level: свободно ${spots.count { it.isFree }} из ${spots.size}")
    }

    println("Свободных компактных: ${parking.freeSpots(SpotType.COMPACT).map { it.id }}")

    println("Активные сессии: ${parking.activeSessions().map { it.plate }}")

    val (session, minutes) = parking.exit("A123BC77", start + 95 * 60_000)!! // TODO антипаттерн !!

    println("Выезд ${session.plate}: $minutes мин, к оплате ${fee(minutes)} руб.")

    println("Занятость после выезда: ${parking.occupancy()}%")

}


