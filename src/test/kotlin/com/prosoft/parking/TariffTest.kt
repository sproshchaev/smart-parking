package com.prosoft.parking

import com.prosoft.parking.tariff.fee
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class TariffTest {

    @ParameterizedTest(name = "{0} мин -> {1} руб.")
    @CsvSource("0, 0",
        "15, 0",
        "16, 60",
        "60, 60",
        "95, 120",
        "120, 120",
        "200, 200"
    )
    fun `тариф округляет неполный час вверх` (minutes: Int, expected: Int) {
        assertEquals(expected, fee(minutes))
    }

    @Test
    fun `ночной тариф вдвое дешевле`() {
        assertEquals(60, fee(95, ratePerHour = 30))
    }

}