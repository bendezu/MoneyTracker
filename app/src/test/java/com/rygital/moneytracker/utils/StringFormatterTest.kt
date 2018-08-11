package com.rygital.moneytracker.utils

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class StringFormatterTest {

    @Test
    fun `Should truncate extra decimals`() {
        assertEquals("15,30", formatMoney(BigDecimal("15.301234")))
    }

    @Test
    fun `Should add tail zero`() {
        assertEquals("15,30", formatMoney(BigDecimal("15.3")))
    }

    @Test
    fun `Should round up`() {
        assertEquals("15,32", formatMoney(BigDecimal("15.319")))
    }
}