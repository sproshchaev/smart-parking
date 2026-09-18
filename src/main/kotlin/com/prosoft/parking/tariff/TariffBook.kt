package com.prosoft.parking.tariff

import com.prosoft.parking.model.SpotType
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readLines

class TariffBook(private val path: Path) {

    val rates: Map<SpotType, Int> by lazy {

        println(" [lazy] читаем тарифы с диска: $path")
        if (!path.exists()) SpotType.entries.associateWith { 60 }
        else path.readLines()
            .filter { it.isNotBlank() }
            .associate { line ->
                val (type, rate) = line.split("=")
                SpotType.valueOf(type.trim()) to rate.trim().toInt()
            }

    }

    fun rateFor(type: SpotType): Int = rates.getValue(type)

}