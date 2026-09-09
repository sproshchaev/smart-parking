package com.prosoft.parking

import com.prosoft.parking.model.Clock
import com.prosoft.parking.model.Spot
import com.prosoft.parking.model.SpotType
import com.prosoft.parking.storage.SessionLog
import io.mockk.every
import org.junit.jupiter.api.Assertions.*
import kotlin.io.path.Path
import io.mockk.mockk
import io.mockk.verify


class ParkingServiceTest {

    private val start = 1_700_000_000_000

    private fun service(clock: Clock, log: SessionLog = SessionLog(Path("build/test/log.csv"))) =
        ParkingService(Parking(listOf(Spot("L1-01", 1, SpotType.COMPACT))), log, clock)


    fun `чек считается по времени из часов`() {
        val clock = mockk<Clock>()
        every { clock.now() } returnsMany listOf(start, start + 95 * 60_000L)

        val service = service(clock)
        service.enter("a123bc77")
        val receipt = service.exit("A123BC77")

        assertEquals(95, receipt.minutes)
        assertEquals(120, receipt.amount)
        verify(exactly = 2) { clock.now() }
    }


}