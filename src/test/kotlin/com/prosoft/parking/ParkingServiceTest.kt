package com.prosoft.parking

import com.prosoft.parking.model.*
import com.prosoft.parking.storage.SessionLog
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.io.path.Path

// TODO прогнать тесты
class ParkingServiceTest {

    private val start = 1_700_000_000_000

    private fun service(clock: Clock, log: SessionLog = SessionLog(Path("build/test/log.csv"))) =
        ParkingService(Parking(listOf(Spot("L1-01", 1, SpotType.COMPACT))), log, clock)


    @Test
    fun `чек считается по времени из часов`() {
        val clock = mockk<Clock>()
        every { clock.now() } returnsMany listOf(start, start + 95 * 60_000L)

        val service = service(clock)
        service.enter("а123вс77")
        val receipt = service.exit("А123ВС77")

        assertEquals(95, receipt.minutes)
        assertEquals(120, receipt.amount)
        verify(exactly = 2) { clock.now() }
    }

    @Test
    fun `нераспознанный номер отклоняется`() {
        val service = service(Clock { start })
        val error = assertThrows<IllegalArgumentException> {
            service.enter("мусор")
        }
        assertEquals("Номер не распознан: мусор", error.message)
    }

    @Test
    fun `повторный въезд запрещен`() {
        val service = service(Clock { start })
        service.enter("А123ВС77")
        assertThrows<IllegalStateException> { service.enter("А123ВС77") }
    }


    @Test
    fun `занятое место не занимается второй раз`() {
        val spot = Spot("L1-01", 1, SpotType.COMPACT)
        spot.occupy(Car("А123ВС77"))
        assertThrows<SpotOccupiedException> { spot.occupy(Car("B456EK99")) }
    }

    @Test
    fun `журнал получает запись о выезде`() {
        val log = mockk<SessionLog>(relaxed = true)
        val clock = mockk<Clock>()
        every { clock.now() } returnsMany listOf(start, start + 60 * 60_000L)
        val service = service(clock, log)
        service.enter("А123ВС77")
        service.exit("А123ВС77")

        verify(exactly = 1) { log.append(any(), 60, 60) }
    }


}