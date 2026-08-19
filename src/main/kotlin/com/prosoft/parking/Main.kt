package com.prosoft.parking

import com.prosoft.parking.model.*
import com.prosoft.parking.tariff.fee

fun main() {

    val vehicle: List<Vehicle> = listOf(
        Car("А123ВС77"),
        Motorcycle("Е789КМ50"),
        Truck("В456ЕК99", axles = 3), // висячая запятая разрешена
    )

    vehicle.forEach {
        println(it.describe() + ", коэффициент ${it.sizeFactor}")
    }

    val spot = Spot("L1-01", level = 1)
    println("Место ${spot.id} свободно: ${spot.isFree}")
    spot.occupy(vehicle[0])
    print("Место ${spot.id} занято ${spot.occupiedBy?.plate}")

    println("К оплате: ${fee(minutes = 95)} руб.")
}


