package com.prosoft.parking

import com.prosoft.parking.model.Car
import com.prosoft.parking.model.Spot
import com.prosoft.parking.model.SpotType
import com.prosoft.parking.storage.SessionLog
import com.prosoft.parking.tariff.fee
import kotlin.io.path.Path
import kotlin.io.path.readText

fun buildSpots(): List<Spot> = buildList {
    repeat(2) { add(Spot("L1-0${it + 1}", 1, SpotType.COMPACT)) }
    add(Spot("L3-01", 3, SpotType.TRUCK))
}


fun main() {

    val parking = Parking(buildSpots())

    val log = SessionLog(Path("build/demo/sessions.csv"))
    log.clear()

    val start = 1_700_000_000_000

    val plan = listOf(
        "A123BC77" to 95, "B456EK99" to 20,
        "E789KM50" to 400, "A123BC77" to 60
    )

    plan.forEach { (plate, minutes) ->
        parking.enterChecked(Car(plate), start).onSuccess {
            val (session, actual) = parking.exit(
                plate, start + minutes * 60_000L
            )
            val amount = fee(actual)
            log.append(session, actual, amount)
            println("$plate: $actual мин -> $amount руб.")
        }

    }

    println("--- файл на диске ---")
    print(Path("build/demo/sessions.csv").readText())
    println("--- итоги ---")
    println("Выручка: ${log.revenue()} руб.")
    println("Топ клиентов: ${log.topPlates(2)}")

}


