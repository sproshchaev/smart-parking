package com.prosoft.parking.shell

import com.prosoft.parking.ParkingService
import com.prosoft.parking.model.AdjustableClock
import org.springframework.context.annotation.Profile
import org.springframework.shell.core.command.annotation.Argument
import org.springframework.stereotype.Component
import org.springframework.shell.core.command.annotation.Command
import org.springframework.shell.core.command.annotation.Option

@Profile("shell")
@Component
class ParkingCommands(
    private val service: ParkingService,
    private val clock: AdjustableClock,
) {

    @Command(name = ["travel"], description = "Перемотать часы вперед: travel 95")
    fun travel(@Argument(index = 0) minutes: Long): String {
        clock.travel(minutes)
        return "Часы сдвинуты на $minutes мин вперед"
    }

    @Command(name = ["enter"], description = "Въезд: enter А123ВС77")
    fun enter(
        @Argument(index = 0) plate: String,
        @Option(longName = "axles", defaultValue = "0") axles: Int,
    ): String =
        runCatching {
            val session = service.enter(plate, axles.takeIf { it > 0 })
            "Добро пожаловать. ${session.plate}. Ваше место ${session.spotId}"
        }.getOrElse { "Отказ: ${it.message}" }

    @Command(name = ["leave"], description = "Выезд: leave А123ВС77")
    fun leave(@Argument(index = 0) plate: String): String =
        runCatching {
            val receipt = service.exit(plate)
            "Чек: ${receipt.plate}, место ${receipt.spotId}, ${receipt.minutes} мин, " +
                    "${receipt.amount} руб."
    }.getOrElse { "" }

    @Command(name = ["status"], description = "Кто сейчас на парковке")
    fun status(): String {
        val active = service.active()
        return if (active.isEmpty()) {
            "Парковка пуста, выручка ${service.revenue()} руб."
        } else {
            active.joinToString("\n") {
                "${it.plate} на ${it.spotId}"
            } + "\nВыручка: ${service.revenue()} руб."
        }
    }

}