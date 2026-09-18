package com.prosoft.parking

import com.prosoft.parking.dsl.parking
import com.prosoft.parking.model.SpotType
import com.prosoft.parking.tariff.TariffBook
import kotlin.io.path.Path
import kotlin.io.path.writeText


fun main() {

    val parking = parking {
        level(1) {
            spots(4, SpotType.COMPACT)
            spots(2, SpotType.STANDARD)
        }
        level(2) {
            spots(3, SpotType.STANDARD)
        }
        level(3) {
            spots(1, SpotType.TRUCK)
        }
    }

    print("Мест построено: ${parking.total}")
    parking.byLevel().forEach { (level, spots) ->
        println(" уровень $level: ${spots.joinToString { "${it.id}/${it.type.title}" }}")
    }

    val file = Path("build/demo/tariffs.txt")
    file.writeText("COMPACT = 60\nSTANDARD = 90\nTRUCK = 200\n")

    val book = TariffBook(file)
    println("До первого обращения файл не читался")
    println("COMPACT: ${book.rateFor(SpotType.COMPACT)} руб/ч")
    println("COMPACT: ${book.rateFor(SpotType.TRUCK)} руб/ч")
    println("COMPACT: ${book.rateFor(SpotType.STANDARD)} руб/ч")


}


