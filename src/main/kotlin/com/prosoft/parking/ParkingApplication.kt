package com.prosoft.parking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import com.prosoft.parking.dsl.parking
import com.prosoft.parking.model.AdjustableClock
import com.prosoft.parking.model.SpotType
import com.prosoft.parking.model.SystemClock
import com.prosoft.parking.storage.SessionLog
import org.springframework.boot.runApplication
import kotlin.io.path.Path


/**
 * Точка входа в приложение на Spring
 */
@SpringBootApplication
class ParkingApplication {

    @Bean
    fun parking(): Parking = parking {
        level(1) {
            spots(2, SpotType.COMPACT)
            spots(1, SpotType.STANDARD)
        }
        level(2) { spots(1, SpotType.TRUCK) }
    }

    @Bean
    fun sessionLog(): SessionLog = SessionLog(Path("build/demo/session.csv"))

    @Bean
    fun clock(): AdjustableClock = AdjustableClock(SystemClock)

}

fun main(args: Array<String>) {
    runApplication<ParkingApplication>(*args)
}
