package com.prosoft.parking.model

fun interface Clock {
    fun now(): Long;
}

object SystemClock : Clock {
    override fun now(): Long = System.currentTimeMillis()
}

class AdjustableClock(private val base: Clock = SystemClock) : Clock {
    var offsetMillis: Long = 0
    override fun now(): Long = base.now() + offsetMillis
    fun travel(minutes: Long) {
        offsetMillis += minutes * 60_000
    }
}