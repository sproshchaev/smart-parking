package com.prosoft.parking.concurrent

import com.prosoft.parking.Parking
import com.prosoft.parking.dsl.parking
import com.prosoft.parking.model.Car
import com.prosoft.parking.model.ParkResult
import com.prosoft.parking.model.SpotType
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

private const val DRIVERS = 30
private const val SPOTS = 20

private fun plateOf(i: Int): String {
    val letters = "АВЕКМНОРСТУХ"
    return "${letters[i % 12]}${(100 + i)}${letters[(i + 1) % 12]}${letters[(i + 2) % 12]}77"
}

private fun naiveBoard() {
    var free = SPOTS
    val threads = List(DRIVERS) {
        thread {
            if (free > 0) {
                Thread.sleep(1)
                free--
            }
        }
    }
    threads.forEach { it.join() }
    println("1. Табло на var:  свободно $free(ожидали 0, отрицательное значение = продали лишнее)")
}

private fun atomicBoard() {
    val free = AtomicInteger(SPOTS)
    val thread = List(DRIVERS) {
        thread {
            while (true) {
                val current = free.get()
                if (current == 0) break
                if (free.compareAndSet(current, current - 1)) break
            }
        }
    }
    thread.forEach { it.join() }
    println("2. Табло на AtomicInteger: свободно ${free.get()} (ожидали 0)")

}

private fun stormParking(parking: Parking, title: String) {
    val ok = AtomicInteger()
    val noSpace = AtomicInteger()
    val crashed = AtomicInteger()

    val threads = List(DRIVERS) { i ->

        thread {
            try {
                when (parking.enter(Car(plateOf(i)), now = 1_700_000_000_000)) {
                    is ParkResult.Ok -> ok.incrementAndGet()
                    ParkResult.NoSpace -> noSpace.incrementAndGet()
                    else -> Unit
                }
            } catch (e: Exception) {
                crashed.incrementAndGet();
            }
        }
    }
    threads.forEach { it.join() }

    println(
        "$title въехало=${ok.get()}, отказов=${noSpace.get()}, исключений=${crashed.get()}, "
                + "сессий в памяти = ${parking.activeSessions().size} " +
                "(мест всего $SPOTS)"
    )

}

fun main() {
    naiveBoard()
    atomicBoard()
    repeat(3) {
        stormParking(parking { level(1) { spots(SPOTS, SpotType.COMPACT) } },
            "3. Штурм парковки: ")
    }
}


