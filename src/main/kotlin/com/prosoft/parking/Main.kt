package com.prosoft.parking

import com.prosoft.parking.model.Car
import com.prosoft.parking.model.ParkResult
import com.prosoft.parking.model.Spot
import com.prosoft.parking.model.SpotType
import com.prosoft.parking.plate.platesInLog
import com.prosoft.parking.plate.recognizePlate
import com.prosoft.parking.storage.SessionLog
import com.prosoft.parking.tariff.fee
import kotlin.io.path.Path
import kotlin.io.path.readText

fun buildSpots(): List<Spot> = buildList {
    repeat(2) { add(Spot("L1-0${it + 1}", 1, SpotType.COMPACT)) }
    add(Spot("L3-01", 3, SpotType.TRUCK))
}


fun main() {

    listOf(" а123вс77 ", "А123ВС77", "A123BC77", "А1234С77", "Х777ХХ199", null)
        .forEach { raw ->
            val plate = recognizePlate(raw)
            println("'${raw ?: "null"}' -> ${plate ?: "не распознан"}")
        }

    println("--- разбор строки камеры ---")
    val line = "12:04 IN А123ВС77 | 12:51 OUT А123ВС77 | 13:02 IN Х777ХХ99 | 13:05 IN ЩУКА"

    platesInLog(line).forEach {
        println("нашли ${it.value}, регион ${it.region}")
    }

    println("--- въезд по сырому номеру ---")
    val parking = Parking(buildSpots())

    val start = 1_700_000_000_000

    listOf(" а123вс77 ", "мусор", "в456ек99").forEach {
        raw ->
        val result = parking.enterByPlate(raw, start) {
            plate -> Car(plate)
        }
        val message = when (result) {
            is ParkResult.Ok -> "${result.session.plate} -> ${result.session.spotId}"
            is ParkResult.AlreadyInside -> "${result.plate} уже внутри"
            is ParkResult.UnknownPlate -> "шлагбаум закрыт, номер '${result.raw}' не читается"
            ParkResult.NoSpace -> "мест нет"
        }
        println(message)
    }

}


