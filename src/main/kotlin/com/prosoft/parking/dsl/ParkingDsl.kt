package com.prosoft.parking.dsl

import com.prosoft.parking.Parking
import com.prosoft.parking.model.Spot
import com.prosoft.parking.model.SpotType

@DslMarker
annotation class ParkingDslMarker

@ParkingDslMarker
class LevelScope(private val level: Int) {

    val spots = mutableListOf<Spot>()

    fun spots(count: Int, type: SpotType) {
        repeat(count) {
            val number = (spots.size + 1)
                .toString().padStart(2, '0')
            spots += Spot("L$level-$number", level, type)
        }
    }

}

@ParkingDslMarker
class ParkingScope {

    private val all = mutableListOf<Spot>()

    fun level(number: Int, block: LevelScope.() -> Unit) {
        all += LevelScope(number).apply(block).spots
    }

    fun build(): Parking = Parking(all)

}

fun parking(block: ParkingScope.() -> Unit): Parking = ParkingScope().apply(block).build()