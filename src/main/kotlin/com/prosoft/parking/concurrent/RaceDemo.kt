package com.prosoft.parking.concurrent

import com.prosoft.parking.Parking
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
                if (free.compareAndSet(current, current -1)) break
            }
        }
    }
    thread.forEach { it.join() }
    println("2. Табло на AtomicInteger: свободно ${free.get()} (ожидали 0)")

}

// TODO завершили здесь 25.09
private fun stormParking(parking: Parking, title: String) {

}